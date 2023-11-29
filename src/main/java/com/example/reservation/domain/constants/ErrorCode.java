package com.example.reservation.domain.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(404, "회원을 찾을 수 없습니다."),
    PARTNER_NOT_FOUND(404, "파트너를 찾을 수 없습니다."),
    ALREADY_EXIST_USER(409, "이미 존재하는 회원입니다."),
    ALREADY_EXIST_PARTNER(409, "이미 존재하는 파트너입니다."),
    INVALID_REFRESH_TOKEN(401, "유효하지 않은 REFRESH 토큰입니다."),
    LOGGED_OUT_USER(401, "로그아웃된 사용자입니다."),
    TOKEN_USER_MISMATCH(401, "토큰의 유저 정보가 일치하지 않습니다."),

    STORE_NOT_FOUND(404, "매장을 찾을 수 없습니다."),

    RESERVATION_NOT_FOUND(404, "예약을 찾을 수 없습니다."),
    ALREADY_EXIST_RESERVATION(409, "해당 시간은 이미 예약이 존재합니다."),
    USER_MISMATCH(401, "예약자와 현재 회원 정보가 일치하지 않습니다."),
    CANCEL_RESERVATION_INVALID_TIME(400, "예약 시간 30분 전까지만 예약 취소가 가능합니다."),
    USER_NOT_ARRIVE(400, "예약자가 정상적으로 도착하지 않아서 예약이 완료되지 않았습니다."),

    REVIEW_NOT_FOUND(404, "리뷰를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 에러가 발생했습니다. 고객센터로 문의 바랍니다.");


    private final int status;
    private final String description;
}
