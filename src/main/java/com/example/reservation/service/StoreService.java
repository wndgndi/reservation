package com.example.reservation.service;

import com.example.reservation.domain.Reservation;
import com.example.reservation.domain.Store;
import com.example.reservation.domain.User;
import com.example.reservation.domain.constants.ReservationStatus;
import com.example.reservation.dto.StoreDto;
import com.example.reservation.dto.StoreDto.Request;
import com.example.reservation.dto.StoreDto.Response;
import com.example.reservation.repository.ReservationRepository;
import com.example.reservation.repository.StoreRepository;
import com.example.reservation.repository.UserRepository;
import com.example.reservation.security.SecurityUtil;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    // 매장 등록
    @Transactional
    public StoreDto.Response register(Request request) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        Store store = storeRepository.save(request.toEntity(request ,user));  // 매장을 저장할 때 점장을 등록

        return StoreDto.fromEntity(store);
    }

    // 매장 조회
    @Transactional
    public StoreDto.Response getStore(Long id) {
        return StoreDto.fromEntity(storeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 매장입니다.")));
    }

    // 매장 수정
    @Transactional
    public StoreDto.Response update(Long id, Request request) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 매장입니다."));

        store.updateStore(request.getName(), request.getAddress(), request.getDescription());
        return StoreDto.fromEntity(storeRepository.save(store));
    }

    // 매장 삭제
    @Transactional
    public String delete(Long id) {
        storeRepository.deleteById(id);
        return "삭제되었습니다.";
    }

    // 매장명에 키워드를 포함하는 매장들 가져오기
    @Transactional(readOnly = true)
    public List<Response> getStores(String keyword) {
        return storeRepository.findByNameContaining(keyword)
            .stream().map(StoreDto::fromEntity).collect(Collectors.toList());
    }

    // 매장에서 예약을 승인할지 거절할지 결정
    @Transactional
    public String decideReservation(String status, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException("예약이 존재하지 않습니다."));
        reservation.updateStatus(ReservationStatus.from(status));
        return reservationService.reservationResult(ReservationStatus.from(status), reservation);
    }

    // 점장이 등록한 매장들 가져오기
    @Transactional(readOnly = true)
    public List<StoreDto.Response> getStoresByPartner(Long partnerId) {
        return storeRepository.findByUserId(partnerId).stream().map(StoreDto::fromEntity).collect(
            Collectors.toList());
    }

}
