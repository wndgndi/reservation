package com.example.reservation.service;

import com.example.reservation.domain.User;
import com.example.reservation.domain.constants.Role;
import com.example.reservation.dto.UserDto;
import com.example.reservation.dto.UserDto.Request;
import com.example.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository UserRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 회원가입 기능
    public UserDto.Response signup(Request request) {
        validateSignup(request);   // 가입 요청 정보 검증
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setRole(Role.PARTNER);

        User user = UserRepository.save(request.toEntity(request));

        return UserDto.fromEntity(user);
    }

    // 파트너 가입 서비스 기능
//    public UserDto.Response UserSignup(UserDto.Request request) {
//
//    }

    // 회원가입 검증
    private void validateSignup(UserDto.Request request) {
        // 이미 존재하는 아이디일 경우, 에러를 던짐
        if(UserRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
    }
}
