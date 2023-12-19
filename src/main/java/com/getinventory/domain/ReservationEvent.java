package com.getinventory.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationEvent {

    public static enum EventType {
        RESERVE,
        RELEASE,
    }

    private final EventType eventType;

    private final Reservation reservation;
}
