package com.example.reservation.service;

import com.example.reservation.domain.RefreshToken;
import com.example.reservation.domain.User;
import com.example.reservation.domain.constants.Role;
import com.example.reservation.dto.TokenDto;
import com.example.reservation.dto.UserDto;
import com.example.reservation.dto.UserDto.Request;
import com.example.reservation.repository.RefreshTokenRepository;
import com.example.reservation.repository.UserRepository;
import com.example.reservation.security.TokenProvider;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 회원가입 기능
    @Transactional
    public UserDto.Response signup(Request request) {
        validateUser(request);   // 가입 요청 정보 검증
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setRole(Role.ROLE_USER);

        User user = userRepository.save(request.toEntity(request));

        return UserDto.fromEntity(user);
    }

    // 로그인 기능
    @Transactional
    public TokenDto login(UserDto.Request request) {
        return getTokenDto(request);
    }

    // 파트너 가입 서비스 기능
    @Transactional
    public TokenDto partnerSignup(UserDto.Request request) {
        User user = validatePartner(request);
        user.updateRole(Role.ROLE_PARTNER);

        UserDto.fromEntity(userRepository.save(user));
        return getTokenDto(request);
    }

    // 토큰 발급
    private TokenDto getTokenDto(Request request) {
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
            .key(authentication.getName())
            .value(tokenDto.getRefreshToken())
            .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    // 토큰을 재발급
    @Transactional
    public TokenDto reissue(TokenDto.Request request) {
        if (!tokenProvider.validateToken(request.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
            .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다."));

        if(!refreshToken.getValue().equals(request.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

    // 회원가입 검사
    private void validateUser(UserDto.Request request) {
        // 이미 존재하는 아이디일 경우, 에러를 던짐
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
    }

    // 이미 가입된 파트너인지 검사
    private User validatePartner(UserDto.Request request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        if(user.getRole() == Role.ROLE_PARTNER) {
            throw new RuntimeException("이미 가입된 파트너입니다.");
        }

        return user;
    }
}
