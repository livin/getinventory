package com.getinventory.service;

import com.getinventory.domain.Reservation;
import com.getinventory.domain.User;
import com.getinventory.repository.ReservationRepository;
import com.getinventory.repository.UserRepository;
import com.getinventory.security.SecurityUtils;
import com.getinventory.web.rest.errors.BadRequestAlertException;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public Reservation createReservation(Reservation reservation) {
        checkUser(reservation);
        return reservationRepository.save(reservation);
    }

    private void checkUser(Reservation reservation) {
        if (SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN")) // bypass
        return;

        Optional<String> login = SecurityUtils.getCurrentUserLogin();
        if (login.isEmpty()) throw new IllegalStateException("Security context doesn't have user");

        Optional<User> currentUser = userRepository.findOneByLogin(login.get());
        if (currentUser.isEmpty()) throw new IllegalStateException("User such login doesn't exist: " + login.get());

        if (!Objects.equals(currentUser.get().getId(), reservation.getUser().getId())) throw new BadRequestAlertException(
            "You can only reserve inventory for yourself",
            "reservation",
            "usermismatch"
        );
    }
}
