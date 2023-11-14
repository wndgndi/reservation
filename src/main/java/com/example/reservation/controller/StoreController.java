package com.example.reservation.controller;

import com.example.reservation.dto.StoreDto;
import com.example.reservation.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreRepository storeRepository;

    @PostMapping("/signup")
    public void signup(@RequestBody StoreDto.Request request) {
        storeRepository.save(request.toEntity(request));
    }
}
