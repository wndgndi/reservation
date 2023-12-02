package com.example.reservation.service;

import com.example.reservation.domain.RefreshToken;
import com.example.reservation.domain.User;
import com.example.reservation.domain.constants.ErrorCode;
import com.example.reservation.domain.constants.Role;
import com.example.reservation.dto.TokenDto;
import com.example.reservation.dto.UserDto;
import com.example.reservation.dto.UserDto.Request;
import com.example.reservation.exception.CustomException;
import com.example.reservation.repository.RefreshTokenRepository;
import com.example.reservation.repository.UserRepository;
import com.example.reservation.security.TokenProvider;
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
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // Access Token 에서 회원 Id를 가져옴
        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

        // 회원 Id를 기반으로 Refresh Token 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
            .orElseThrow(() -> new CustomException(ErrorCode.LOGGED_OUT_USER));

        // Refresh Token 값이 일치하는지 검사
        if(!refreshToken.getValue().equals(request.getRefreshToken())) {
            throw new CustomException(ErrorCode.TOKEN_USER_MISMATCH);
        }

        // 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // db에 Refresh Token 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    // 회원가입 검사
    private void validateUser(UserDto.Request request) {
        // 이미 존재하는 아이디일 경우, 에러를 던짐
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
        }
    }

    // 이미 가입된 파트너인지 검사
    private User validatePartner(UserDto.Request request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new CustomException(ErrorCode.ALREADY_EXIST_USER));

        if(user.getRole() == Role.ROLE_PARTNER) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_PARTNER);
        }

        return user;
    }
}
