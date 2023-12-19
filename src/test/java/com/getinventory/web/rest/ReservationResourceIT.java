package com.getinventory.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getinventory.IntegrationTest;
import com.getinventory.config.EmbeddedKafka;
import com.getinventory.domain.Inventory;
import com.getinventory.domain.Reservation;
import com.getinventory.domain.ReservationEvent;
import com.getinventory.domain.User;
import com.getinventory.repository.InventoryRepository;
import com.getinventory.repository.ReservationRepository;
import com.getinventory.repository.UserRepository;
import com.getinventory.service.ReservationEventService;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReservationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
@EmbeddedKafka
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
class ReservationResourceIT {

    private static final String DEFAULT_RESERVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_RESERVED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_RESERVED_AT = Instant.ofEpochMilli(0L);

    private static final String ENTITY_API_URL = "/api/reservations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    public static final String JOHNTESTER_SAMPLE_LOGIN = "johntester";
    public static final String JULIETESTER_SAMPLE_LOGIN = "julietester";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservationMockMvc;

    @Autowired
    private OutputDestination output;

    @Autowired
    private ObjectMapper objectMapper;

    private Reservation reservation;
    private Inventory sampleInventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public Reservation createEntity(EntityManager em) {
        Reservation reservation = Reservation
            .builder()
            .inventory(sampleInventory)
            .user(getSampleUser())
            .reservedAt(DEFAULT_RESERVED_AT)
            .build();
        return reservation;
    }

    private Inventory createSampleInventory() {
        return Inventory.builder().id(1005L).name("MacBook Pro").build();
    }

    private static User getSampleUser() {
        return User.builder().id(2L).login("johntester").build();
    }

    @BeforeEach
    public void initTest() {
        sampleInventory = inventoryRepository.saveAndFlush(createSampleInventory());
        reservation = createEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser(value = JOHNTESTER_SAMPLE_LOGIN, roles = { "USER" })
    void createReservation() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // ensure no reserved at specified
        reservation.setReservedAt(null);

        // Create the Reservation
        restReservationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservation))
            )
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notNullValue()))
            .andExpect(jsonPath("$.reservedAt").value(notNullValue()))
            .andExpect(jsonPath("$.inventory.id").value(sampleInventory.getId()))
            .andExpect(jsonPath("$.user.id").value(getSampleUser().getId()));

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate + 1);

        ReservationEvent event = readEvent(output.receive(1000, ReservationEventService.RESERVATIONS_KAFKA_TOPIC));
        assertThat(event.getEventType()).isEqualTo(ReservationEvent.EventType.RESERVE);
        assertThat(event.getReservation().getInventory().getName()).isEqualTo("MacBook Pro");
        assertThat(event.getReservation().getUser().getId()).isEqualTo(getSampleUser().getId());
    }

    private ReservationEvent readEvent(Message<byte[]> event) throws IOException {
        assertThat(event).isNotNull();
        return objectMapper.readValue(event.getPayload(), ReservationEvent.class);
    }

    @Test
    @Transactional
    @WithMockUser(username = JOHNTESTER_SAMPLE_LOGIN, roles = { "USER" })
    void createReservation_forAnotherUser_prohibited() throws Exception {
        User anotherTester = userRepository.findOneByLogin(JULIETESTER_SAMPLE_LOGIN).get();

        // tester attempts to reserve for anothertester
        Reservation reservationForAnotherUser = Reservation
            .builder()
            .inventory(sampleInventory)
            .user(anotherTester)
            .reservedAt(Instant.now())
            .build();

        // Create the Reservation
        restReservationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservationForAnotherUser))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void createReservationWithExistingId() throws Exception {
        // Create the Reservation with an existing ID
        reservation.setId(1L);

        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReservations() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList
        restReservationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].user.id").value(hasItem(reservation.getUser().getId().intValue())))
            .andExpect(jsonPath("$.[*].reservedAt").value(notNullValue()));
    }

    @Test
    @Transactional
    void getReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get the reservation
        restReservationMockMvc
            .perform(get(ENTITY_API_URL_ID, reservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reservation.getId().intValue()))
            .andExpect(jsonPath("$.user.id").value(reservation.getUser().getId()))
            .andExpect(jsonPath("$.reservedAt").value(DEFAULT_RESERVED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReservation() throws Exception {
        // Get the reservation
        restReservationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void deleteReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeDelete = reservationRepository.findAll().size();

        // Delete the reservation
        restReservationMockMvc
            .perform(delete(ENTITY_API_URL_ID, reservation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
