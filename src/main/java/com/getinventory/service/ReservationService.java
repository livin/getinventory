package com.getinventory.service;

import com.getinventory.domain.Reservation;
import com.getinventory.domain.User;
import com.getinventory.repository.ReservationRepository;
import com.getinventory.repository.UserRepository;
import com.getinventory.security.SecurityUtils;
import com.getinventory.web.rest.errors.BadRequestAlertException;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Reservation createReservation(Reservation reservation) {
        checkUser(reservation);

        reservation.setReservedAt(Instant.now());

        return reservationRepository.save(reservation);
    }

    private void checkUser(Reservation reservation) {
        if (SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN")) return; // bypass

        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.isEmpty()) throw new IllegalStateException("Security context doesn't have user");

        Optional<User> currentUser = userRepository.findOneByLogin(login.get());
        if (currentUser.isEmpty()) throw new IllegalStateException("User such login doesn't exist: " + login.get());

        if (!Objects.equals(currentUser.get().getId(), reservation.getUser().getId())) throw new BadRequestAlertException(
            "Reservations can be operated only by owner",
            "reservation",
            "user.prohibited"
        );
    }

    @Transactional
    public void deleteById(Long id) {
        checkUser(reservationRepository.findById(id).orElseThrow());
        reservationRepository.deleteById(id);
    }
}
