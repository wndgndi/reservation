package com.example.reservation.dto;

import com.example.reservation.domain.Partner;
import com.example.reservation.domain.Store;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class StoreDto {

    @Getter
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private String name;

        @NotNull
        private String address;

        @NotNull
        private String description;

        public Store toEntity(Request request) {
            return new Store(request.getName(), request.getAddress(), request.getDescription());
        }
    }

    public static class Response {
        private long id;
        private String name;
        private String address;
        private String description;
        private Partner partner;
    }
}
