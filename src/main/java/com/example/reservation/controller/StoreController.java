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

    @PostMapping("/register")
    public ResponseEntity<StoreDto.Response> register(@RequestBody StoreDto.Request request) {
        return ResponseEntity.ok(storeService.register(request));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StoreDto.Response> update(@PathVariable Long id, @RequestBody StoreDto.Request request) {
        return ResponseEntity.ok(storeService.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.delete(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<StoreDto.Response>> getStoreInfo(@RequestParam String keyword) {
        return ResponseEntity.ok(storeService.getStoreInfo(keyword));
    }

    // 파트너가 여러개의 매장을 가지고 있을 경우, 모든 매장 조회
    @GetMapping("/partner/{id}")
    public ResponseEntity<List<StoreDto.Response>> getStores(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.getStores(id));
    }
}
