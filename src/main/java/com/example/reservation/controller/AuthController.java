package com.example.reservation.controller;

import com.example.reservation.dto.TokenDto;
import com.example.reservation.dto.UserDto;
import com.example.reservation.service.AuthService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // 회원 가입 기능
    @PostMapping("/user/signup")
    public ResponseEntity<UserDto.Response> signup(@RequestBody @Valid UserDto.Request request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    // 로그인 기능
    @PostMapping("/user/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid UserDto.Request request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // 회원 가입 이후에 파트너 가입 가능
    @PostMapping("/partner/signup")
    public ResponseEntity<TokenDto> partnerSignup(@RequestBody @Valid UserDto.Request request) {
        return ResponseEntity.ok(authService.partnerSignup(request));
    }

    // 토큰 재발급 기능
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenDto.Request request) {
        return ResponseEntity.ok(authService.reissue(request));
    }
}
