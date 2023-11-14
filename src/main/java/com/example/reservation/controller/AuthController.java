package com.example.reservation.controller;

import com.example.reservation.dto.PartnerDto;
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

    // 파트너 가입 기능
    @PostMapping("/manager/signup")
    public ResponseEntity<PartnerDto.Response> ManagerSignup(@RequestBody @Valid PartnerDto.Request request) {
        return ResponseEntity.ok(authService.partnerSignup(request));
    }
}
