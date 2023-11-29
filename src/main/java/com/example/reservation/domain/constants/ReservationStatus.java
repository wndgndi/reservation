package com.example.reservation.domain.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReservationStatus {
    PENDING,  // 예약 대기
    CONFIRMED,   // 예약 승인
    CANCELED,  // 예약 거절
    END,    // 예약, 도착 완료
    LATE;  // 예약 시간 미준수


    @JsonCreator
    public static ReservationStatus from(String s) {
        return ReservationStatus.valueOf(s.toUpperCase());
    }
}
