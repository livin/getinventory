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
    void testInventoryAvailabilityProvided() {
        fail(
            "Inventory availability feature is not implemented yet - test should be written." +
            "It should be able to see available inventory with available flag and reservation info if any"
        );
    }

    @Test
    @DisplayName("Emit reservation events via message broker")
    void sendEventsAboutReservationsToMessageBroker() {
        fail(
            """
            Integrate message broker (kafka).
            Add docker runnable for broker.
            Add tooling to test broker messages.
            Send reservation events to broker.
            """
        );
    }

    @Test
    @DisplayName("Use JWT Tokens for easy auth by other microservices")
    void jwtTokens() {
        fail("Migrate to JWT Token authentication vs default spring security auth");
    }

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
