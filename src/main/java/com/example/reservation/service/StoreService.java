package com.example.reservation.service;

import com.example.reservation.domain.Store;
import com.example.reservation.domain.User;
import com.example.reservation.dto.StoreDto;
import com.example.reservation.dto.StoreDto.Request;
import com.example.reservation.dto.StoreDto.Response;
import com.example.reservation.repository.StoreRepository;
import com.example.reservation.repository.UserRepository;
import com.example.reservation.security.SecurityUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreDto.Response register(Request request) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        Store store = storeRepository.save(request.toEntity(request ,user));

        return StoreDto.fromEntity(store);
    }

    @Transactional
    public StoreDto.Response update(Long id, Request request) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new RuntimeException("존재하지 않는 매장입니다."));

        store.updateStore(request.getName(), request.getAddress(), request.getDescription());
        return StoreDto.fromEntity(storeRepository.save(store));
    }

    @Transactional
    public String delete(Long id) {
        storeRepository.deleteById(id);
        return "삭제되었습니다.";
    }

    @Transactional(readOnly = true)
    public List<Response> getStoreInfo(String keyword) {
        return storeRepository.findByNameContaining(keyword)
            .stream().map(StoreDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreDto.Response> getStores(Long id) {
        return storeRepository.findByUserId(id).stream().map(StoreDto::fromEntity).collect(
            Collectors.toList());
    }

}
