package com.example.reservation.security;

import com.example.reservation.domain.User;
import com.example.reservation.dto.TokenDto;
import com.example.reservation.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenProvider {

    private static final String KEY_ROLES = "role";  // 토큰의 클레임 중에서 권한 정보를 나타내느데 사용
    private static final String BEARER_TYPE = "bearer";  // JWT 토큰 생성 및 검증 시에 사용되는 상수
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;  //  Access Token 의 만료시간을 30분으로 설정
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60* 24 * 7;  //  Refresh Token 의 만료시간을 7일로 설정
    private final UserRepository userRepository;

    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey, UserRepository userRepository) {
        this.userRepository = userRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);  // Base64로 인코딩 된 secretKey 를 디코딩하여 keyBytes 배열에 저장 (시크릿 키를 바이트 배열로 변환)
        this.key = Keys.hmacShaKeyFor(keyBytes);  // JWT 를 서명할 때 사용되고, 서명된 토큰을 검증할 때 필요한  HMAC SHA 알고리즘을 적용한 key 를 생성
    }

    // 유저 정보를 통해 Access Token 과 Refresh Token 생성
    public TokenDto generateTokenDto(Authentication authentication) {
        String authorities = authentication.getAuthorities()  // 현재 인증된 사용자 정보에서 권한 목록을 가져옴
            .stream()
            .map(GrantedAuthority::getAuthority)  // 권한 객체를 문자열로 변환
            .collect(Collectors.joining(","));  // 모든 문자열 쉼표로 연결
        // 사용자가 가지고 있는 모든 권한이 쉼표로 구분되어 들어가게 함


        long now = new Date().getTime();  // 현재 시간을 밀리초 단위로 얻어와서 now 변수에 저장

        Date accessTokenExpireIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim(KEY_ROLES, authorities)
            .setIssuedAt(new Date())   // 토큰 생성 시간
            .setExpiration(accessTokenExpireIn)  // Access Token 만료 시간
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();

        User user = userRepository.findById(Long.parseLong(authentication.getName()))
            .orElseThrow(() -> new RuntimeException("조회 불가"));

        String refreshToken = Jwts.builder()
            .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();

        return TokenDto.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .accessTokenExpiresIn(accessTokenExpireIn.getTime())
            .refreshToken(refreshToken)
            .build();
    }


    public Authentication getAuthentication(String accessToken) {
        // 토큰을 복호화하여 토큰에 들어있는 정보를 꺼냄
        Claims claims = parseClaims(accessToken);


        if(claims.get(KEY_ROLES) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> roles =
            Arrays.stream(claims.get(KEY_ROLES).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", roles);

        return new UsernamePasswordAuthenticationToken(principal, "", roles);
    }

    // 토큰 정보를 검증
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e){
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
