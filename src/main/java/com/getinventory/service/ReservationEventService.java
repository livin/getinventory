package com.getinventory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getinventory.domain.Reservation;
import com.getinventory.domain.ReservationEvent;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReservationEventService {

    public static final String RESERVATIONS_KAFKA_TOPIC = "reservations";

    private final StreamBridge streamBridge;

    private final ObjectMapper objectMapper;

    public void notifyReservationCreated(Reservation reservation) {
        sendReservationEvent(reservation, ReservationEvent.EventType.RESERVE);
    }

    public void notifyReservationReleased(Reservation reservation) {
        sendReservationEvent(reservation, ReservationEvent.EventType.RELEASE);
    }

    private void sendReservationEvent(Reservation reservation, ReservationEvent.EventType eventType) {
        ReservationEvent event = ReservationEvent.builder().eventType(eventType).reservation(reservation).build();

        try {
            streamBridge.send(RESERVATIONS_KAFKA_TOPIC, objectMapper.writeValueAsBytes(event));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
