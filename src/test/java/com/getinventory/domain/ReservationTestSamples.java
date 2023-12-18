package com.getinventory.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reservation getReservationSample1() {
        return Reservation.builder().id(1L).user(getSampleUser()).build();
    }

    private static User getSampleUser() {
        return User.builder().id(1001L).login("tester").build();
    }

    public static Reservation getReservationSample2() {
        return Reservation.builder().id(2L).build();
    }
}
