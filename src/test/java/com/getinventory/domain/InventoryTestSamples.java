package com.getinventory.domain;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Inventory getInventorySample() {
        return new Inventory().id(1L).name("name1");
    }

    public static Inventory getReservedInventorySample() {
        return new Inventory()
            .id(2L)
            .name("name2")
            .reservation(new Reservation().id(110L).reservedAt(Instant.now()).user(User.builder().id(1L).login("tester").build()));
    }

    public static Inventory getInventoryRandomSampleGenerator() {
        return new Inventory().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
