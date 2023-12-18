package com.getinventory.domain;

import static com.getinventory.domain.InventoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.getinventory.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventory.class);
        Inventory inventory1 = getInventorySample();
        Inventory inventory2 = new Inventory();
        assertThat(inventory1).isNotEqualTo(inventory2);

        inventory2.setId(inventory1.getId());
        assertThat(inventory1).isEqualTo(inventory2);

        inventory2 = getReservedInventorySample();
        assertThat(inventory1).isNotEqualTo(inventory2);
    }

    @Test
    void inventory_whenHasNoReservation_shouldBeAvailable() {
        assertTrue(getInventorySample().isAvailable());
    }

    @Test
    void inventory_withReservation_shouldBeNotAvailable() {
        assertFalse(getReservedInventorySample().isAvailable());
    }
}
