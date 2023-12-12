package com.getinventory.domain;

import static com.getinventory.domain.InventoryTestSamples.*;
import static com.getinventory.domain.ReservationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.getinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventory.class);
        Inventory inventory1 = getInventorySample1();
        Inventory inventory2 = new Inventory();
        assertThat(inventory1).isNotEqualTo(inventory2);

        inventory2.setId(inventory1.getId());
        assertThat(inventory1).isEqualTo(inventory2);

        inventory2 = getInventorySample2();
        assertThat(inventory1).isNotEqualTo(inventory2);
    }

    @Test
    void reservationTest() throws Exception {
        Inventory inventory = getInventoryRandomSampleGenerator();
        Reservation reservationBack = getReservationRandomSampleGenerator();

        inventory.setReservation(reservationBack);
        assertThat(inventory.getReservation()).isEqualTo(reservationBack);
        assertThat(reservationBack.getInventory()).isEqualTo(inventory);

        inventory.reservation(null);
        assertThat(inventory.getReservation()).isNull();
        assertThat(reservationBack.getInventory()).isNull();
    }
}
