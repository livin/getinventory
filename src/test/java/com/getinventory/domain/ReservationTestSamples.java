package com.getinventory.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reservation getReservationSample1() {
        var user = new User();
        user.setId(1L);
        user.setLogin("tester");
        return new Reservation().id(1L).user(user);
    }

    public static Reservation getReservationSample2() {
        return new Reservation().id(2L);
    }

    public static Reservation getReservationRandomSampleGenerator() {
        return new Reservation().id(longCount.incrementAndGet());
    }
}
