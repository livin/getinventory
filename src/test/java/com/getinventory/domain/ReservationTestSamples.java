package com.getinventory.domain;

import static com.getinventory.domain.InventoryTestSamples.getInventorySample;
import static com.getinventory.domain.UserTestSamples.getSampleUser;

public class ReservationTestSamples {

    public static Reservation getReservationSample1() {
        return Reservation.builder().id(1L).inventory(getInventorySample()).user(getSampleUser()).build();
    }

    public static Reservation getReservationSample2() {
        return Reservation.builder().id(2L).inventory(getInventorySample()).build();
    }
}
