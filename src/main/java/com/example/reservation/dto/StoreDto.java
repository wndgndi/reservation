package com.example.reservation.dto;

import com.example.reservation.domain.Store;
import com.example.reservation.domain.User;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class StoreDto {

    @Getter
    @AllArgsConstructor
    public static class Request {
        private Long id;

        @NotNull
        private String name;

        @NotNull
        private String address;

        @NotNull
        private String description;

        public Store toEntity(Request request, User user) {
            return new Store(request.getName(), request.getAddress(), request.getDescription(), user);
        }
    }

    @Getter
    public static class Response {
        private String name;
        private String address;
        private String description;
        private String managerName;

        public Response(String name, String address, String description, String managerName) {
            this.name = name;
            this.address = address;
            this.description = description;
            this.managerName = managerName;
        }
    }

    public static Response fromEntity(Store store) {
        return new Response(store.getName(), store.getAddress(), store.getDescription(), store.getUser().getName());
    }
}
