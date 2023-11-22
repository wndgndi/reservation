package com.example.reservation.dto;

import com.example.reservation.domain.User;
import com.example.reservation.domain.constants.Role;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserDto {
    @Getter
    @Setter
    public static class Request {
        @Size(min = 2, max = 10)
        private String name;

        @NotNull
        private String username;

        @NotNull
        private String password;

        private String phoneNumber;

        @Enumerated(EnumType.STRING)
        private Role role;

        public User toEntity(UserDto.Request request) {
            return new User(request.getName(), request.getUsername(), request.getPassword(), request.getPhoneNumber(), request.getRole());
        }

        // 로그인 요청 시에 사용되는 데이터를 Spring Security 가 이해할 수 있는 형태로 변환
        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(this.username, this.password);
        }
    }

    @Getter
    public static class Response {
        private long id;
        private String name;
        private String username;
        private String phoneNumber;

        public Response(String name, String username, String phoneNumber) {
            this.name = name;
            this.username = username;
            this.phoneNumber = phoneNumber;
        }
    }

    public static Response fromEntity(User user) {
        return new Response(user.getName(), user.getUsername(), user.getPhoneNumber());
    }
}
