package com.getinventory.domain;

import java.util.Set;

public class InventoryTestSamples {

    public static Inventory getInventorySample() {
        return Inventory.builder().id(1L).name("name1").quantity(1).build();
    }

    public static Inventory getReservedInventorySample() {
        var inventory = Inventory.builder().id(2L).name("name2").quantity(1).build();

        Reservation reservation = Reservation.builder().inventory(inventory).user(User.builder().id(2L).build()).build();

        inventory.reservations(Set.of(reservation));
        return inventory;
    }
}
