package com.example.reservation.dto;

import com.example.reservation.domain.User;
import com.example.reservation.domain.Store;
import com.example.reservation.domain.User;
import com.example.reservation.domain.constants.Role;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class UserDto {
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

        @Enumerated(EnumType.STRING)
        private Role role;

        public User toEntity(UserDto.Request request) {
            return new User(request.getName(), request.getUsername(), request.getPassword(), request.getRole());
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

    public static Response fromEntity(User User) {
        return new Response(User.getName(), User.getUsername());
    }
}
