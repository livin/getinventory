package com.getinventory.service;

import com.getinventory.domain.Inventory;
import com.getinventory.domain.Reservation;
import com.getinventory.domain.User;
import com.getinventory.repository.InventoryRepository;
import com.getinventory.repository.ReservationRepository;
import com.getinventory.repository.UserRepository;
import com.getinventory.security.SecurityUtils;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final ReservationEventService reservationEventService;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Reservation createReservation(Reservation reservation) {
        checkUser(reservation);
        checkAvailability(reservation.getInventory().getId());

        reservation.setReservedAt(Instant.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        reservationEventService.notifyReservationCreated(reservation);

        return savedReservation;
    }

    private void checkAvailability(Long inventoryId) {
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);
        if (inventory.isEmpty()) throw new IllegalStateException("Inventory not found with id: " + inventoryId);

        if (!inventory.get().isAvailable()) throw new IllegalStateException("Inventory is out of stock");
    }

    private void checkUser(Reservation reservation) {
        if (SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN")) return; // bypass

        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.isEmpty()) throw new IllegalStateException("Security context doesn't have user");

        Optional<User> currentUser = userRepository.findOneByLogin(login.get());
        if (currentUser.isEmpty()) throw new IllegalStateException("User such login doesn't exist: " + login.get());

        if (!Objects.equals(currentUser.get().getId(), reservation.getUser().getId())) throw new IllegalStateException(
            "Reservations can be operated only by owner"
        );
    }

    @Transactional
    public void deleteById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        checkUser(reservation);
        reservationRepository.deleteById(id);
        reservationEventService.notifyReservationReleased(reservation);
    }
}
