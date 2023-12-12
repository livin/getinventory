package com.getinventory.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Inventory getInventorySample1() {
        return new Inventory().id(1L).name("name1");
    }

    public static Inventory getInventorySample2() {
        return new Inventory().id(2L).name("name2");
    }

    public static Inventory getInventoryRandomSampleGenerator() {
        return new Inventory().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
