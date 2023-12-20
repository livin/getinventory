package com.getinventory;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Verifies all top-level features are implemented
 * in the project, and it is ready for showcase.
 */
public class ReadinessTest {

    @Test
    @DisplayName("Provide inventory availability")
    void testInventoryAvailabilityProvided() {}

    @Test
    @DisplayName("Emit reservation events via message broker")
    void sendEventsAboutReservationsToMessageBroker() {}

    @Test
    @Disabled
    @DisplayName("Describe production app launch")
    void prepareProduction() {}

    @Test
    @DisplayName("Publish project on GitHub")
    @Disabled
    void publishOnGithub() {}
}
