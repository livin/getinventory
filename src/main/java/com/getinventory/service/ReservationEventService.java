package com.getinventory.service;

import com.getinventory.domain.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReservationEventService {

    public static final String RESERVATIONS_KAFKA_TOPIC = "reservations";

    private final StreamBridge streamBridge;

    public void notifyReservationCreated(Reservation reservation) {
        streamBridge.send(RESERVATIONS_KAFKA_TOPIC, "{ \"event\": \"reserve\"}");
    }
}
