package com.example.reservation.service;

import com.example.reservation.domain.Reservation;
import com.example.reservation.domain.Store;
import com.example.reservation.domain.User;
import com.example.reservation.domain.constants.ErrorCode;
import com.example.reservation.domain.constants.ReservationStatus;
import com.example.reservation.dto.ReservationDto;
import com.example.reservation.dto.ReservationDto.Response;
import com.example.reservation.exception.CustomException;
import com.example.reservation.repository.ReservationRepository;
import com.example.reservation.repository.StoreRepository;
import com.example.reservation.repository.UserRepository;
import com.example.reservation.security.SecurityUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    // 예약 등록
    @Transactional
    public ReservationDto.Response makeReservation(ReservationDto.Request request, Long storeId) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new CustomException(
            ErrorCode.USER_NOT_FOUND));
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if (reservationRepository.findByReservationTimeAndStore(request.getReservationTime(), store).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_RESERVATION);
        }

        Reservation myReservation = reservationRepository.save(request.toEntity(request, user, store));

        return ReservationDto.fromEntity(myReservation);
    }

    // 매장의 예약 승인/거절에 따른 결과 반환
    @Transactional
    public String reservationResult(ReservationStatus status, Reservation reservation) {
        if(status == ReservationStatus.CONFIRMED) {
            return "예약되었습니다.";
        } else {
            reservationRepository.delete(reservation);
            return reservation.getUser().getName() + "님의 " + reservation.getStore().getName() + " " + reservation.getReservationTime() + "시 예약이 불가능합니다.";
        }
    }

    // 예약 삭제
    public Response deleteReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        // 예약이 승인된 상태이고, 예약시간이 30분 이상 남지 않았다면, 예약 취소 불가
        if(reservation.getStatus() == ReservationStatus.CONFIRMED && LocalDateTime.now().isAfter(reservation.getReservationTime().minusMinutes(30))) {
            throw  new CustomException(ErrorCode.CANCEL_RESERVATION_INVALID_TIME);
        }
        reservationRepository.deleteById(reservationId);
        return ReservationDto.fromEntity(reservation);
    }

    // 예약자 도착
    public Response arrive(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new CustomException(
            ErrorCode.RESERVATION_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();   // 현재 시간
        LocalDateTime reservationTime = reservation.getReservationTime();    // 예약 시간
        boolean check = true;  // 메세지를 입력하기 위한 변수

        if(now.isBefore(reservationTime.minusMinutes(10))) {  // 도착 시간이 예약 시간보다 10분 이전이라면,
            reservation.arrive(ReservationStatus.END);   // 도착했으므로 예약 종료 상태로 저장
        } else {
            reservation.arrive(ReservationStatus.LATE);  // 늦었으므로 예약 늦음 상태로 저장
            check = false;
        }
        reservationRepository.save(reservation);
        ReservationDto.Response response = ReservationDto.fromEntity(reservation);
        response.inputMessage(check ? "예약이 확인되었습니다." : "도착 시간 미준수로 인해 예약이 불가능합니다.");

        return response;
    }

}
