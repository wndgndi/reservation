package com.example.reservation.dto;

import com.example.reservation.domain.Reservation;
import com.example.reservation.domain.Review;
import lombok.Getter;

public class ReviewDto {

    @Getter
    public static class Request {
        private String content;

        public Review toEntity(Request request, Reservation reservation) {
            return new Review(request.getContent(), reservation);
        }
    }

    @Getter
    public static class Response {
        private String content;
        private String username;
        private String storeName;

        public Response(String content, String username, String storeName) {
            this.content = content;
            this.username = username;
            this.storeName = storeName;
        }
    }

    public static Response fromEntity(Review review) {
        return new Response(review.getContent(), review.getReservation().getUser().getUsername(), review.getReservation().getStore().getName());
    }
}
