package com.example.reservation.controller;

import com.example.reservation.dto.StoreDto;
import com.example.reservation.service.StoreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    // 매장 등록
    @PostMapping
    public ResponseEntity<StoreDto.Response> register(@RequestBody StoreDto.Request request) {
        return ResponseEntity.ok(storeService.register(request));
    }

    // 매장 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDto.Response> getStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.getStore(storeId));
    }

    // 매장 수정
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreDto.Response> update(@PathVariable Long storeId,
        @RequestBody StoreDto.Request request) {
        return ResponseEntity.ok(storeService.update(storeId, request));
    }

    // 매장 삭제
    @DeleteMapping("/{storeId}")
    public ResponseEntity<String> delete(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.delete(storeId));
    }

    // 매장에서 예약을 승인할지 거절할지 결정
    @PutMapping("/reservation/{reservationId}")
    public ResponseEntity<String> decideReservation(@RequestBody String status, @PathVariable Long reservationId) {
       return ResponseEntity.ok(storeService.decideReservation(status, reservationId));
    }

    // 매장명에 키워드를 포함한 매장 조회
    @GetMapping("/search")
    public ResponseEntity<List<StoreDto.Response>> getStoreInfo(@RequestParam String keyword) {
        return ResponseEntity.ok(storeService.getStores(keyword));
    }

    // 파트너가 여러개의 매장을 가지고 있을 경우, 모든 매장 조회
    @GetMapping("/partner/{partnerId}")
    public ResponseEntity<List<StoreDto.Response>> getStoresByPartner(
        @PathVariable Long partnerId) {
        return ResponseEntity.ok(storeService.getStoresByPartner(partnerId));
    }


}
