package com.example.reservation.controller;

import com.example.reservation.dto.ReservationDto;
import com.example.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    // 예약 신청
    @PostMapping("/{storeId}")
    public ResponseEntity<ReservationDto.Response> makeReservation(@RequestBody ReservationDto.Request request, @PathVariable Long storeId) {
        return ResponseEntity.ok(reservationService.makeReservation(request, storeId));
    }

    // 예약 삭제
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ReservationDto.Response> deleteReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.deleteReservation(reservationId));
    }

    // 예약자 도착
    @PutMapping("/{reservationId}")
    public ResponseEntity<ReservationDto.Response> arrive(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.arrive(reservationId));
    }
}
