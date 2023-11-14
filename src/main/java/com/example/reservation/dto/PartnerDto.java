package com.example.reservation.dto;

import com.example.reservation.domain.Partner;
import com.example.reservation.domain.Store;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class PartnerDto {
    @Getter
    @Setter
    public static class Request {
        @NotNull
        @Size(min = 2, max = 10)
        private String name;

        @NotNull
        private String username;

        @NotNull
        private String password;

        public Partner toEntity(PartnerDto.Request request) {
            return new Partner(request.getName(), request.getUsername(), request.getPassword());
        }
    }

    @Getter
    public static class Response {
        private long id;
        private String name;
        private String username;
        private List<Store> stores;

        public Response(String name, String username) {
            this.name = name;
            this.username = username;
        }
    }

    public static Response fromEntity(Partner partner) {
        return new Response(partner.getName(), partner.getUsername());
    }
}
