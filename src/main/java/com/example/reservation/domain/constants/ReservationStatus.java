package com.example.reservation.domain.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReservationStatus {
    PENDING, CONFIRMED, CANCELED, END, LATE;

    @JsonCreator
    public static ReservationStatus from(String s) {
        return ReservationStatus.valueOf(s.toUpperCase());
    }
}
