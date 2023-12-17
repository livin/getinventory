package com.getinventory.repository;

import com.getinventory.domain.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("select reservation from Reservation reservation where reservation.user.login = ?#{authentication.name}")
    List<Reservation> findByUserIsCurrentUser();
}
