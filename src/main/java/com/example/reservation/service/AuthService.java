package com.example.reservation.service;

import com.example.reservation.domain.Partner;
import com.example.reservation.dto.PartnerDto;
import com.example.reservation.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;

    // 파트너 가입 서비스 기능
    public PartnerDto.Response partnerSignup(PartnerDto.Request request) {
        validateSignup(request);   // 가입 요청 정보 검증

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Partner partner = partnerRepository.save(request.toEntity(request));

        return PartnerDto.fromEntity(partner);
    }

    private void validateSignup(PartnerDto.Request request) {
        // 이미 존재하는 아이디일 경우, 에러를 던짐
        if(partnerRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
    }
}
