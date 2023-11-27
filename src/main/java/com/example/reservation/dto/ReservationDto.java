package com.example.reservation.dto;

import com.example.reservation.domain.Reservation;
import com.example.reservation.domain.Store;
import com.example.reservation.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;

public class ReservationDto {
    private static final String datePattern = "yyyy-MM-dd HH:mm:ss.SSS";

    @Getter
    public static class Request {
        @JsonFormat(pattern = datePattern)
        private LocalDateTime reservationTime;
        private LocalDateTime arrivedAt;
        private User user;
        private Store store;

        public Reservation toEntity(ReservationDto.Request request, User user, Store store) {
            return new Reservation(request.getReservationTime(), user, store);
        }
    }

    @Getter
    public static class Response {
        @JsonFormat(pattern = datePattern)
        private LocalDateTime reservationTime;
        private LocalDateTime arrivedAt;
        private String message;
        private String username;
        private String storeName;

        public Response(LocalDateTime reservationTime, LocalDateTime arrivedAt, String username, String storeName) {
            this.reservationTime = reservationTime;
            this.arrivedAt = arrivedAt;
            this.username = username;
            this.storeName = storeName;
        }

        public void inputMessage(String message) {
            this.message = message;
        }
    }

    public static ReservationDto.Response fromEntity(Reservation reservation) {
        return new ReservationDto.Response(reservation.getReservationTime(), reservation.getArrivedAt(), reservation.getUser().getUsername(), reservation.getStore().getName());
    }
}
