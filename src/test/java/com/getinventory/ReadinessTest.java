package com.getinventory;

import static org.junit.jupiter.api.Assertions.fail;

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
    @DisplayName("Describe production app launch")
    void prepareProduction() {
        fail("Test production version of app. Write down instructions on how to launch it.");
    }

    @Test
    @DisplayName("Publish project on GitHub")
    void publishOnGithub() {
        fail("Publish on github");
    }
}
