package com.example.reservation.repository;

import com.example.reservation.domain.Store;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    // 매장명에서 특정 키워드를 포함하는 매장을 찾습니다.
    List<Store> findByNameContaining (String keyword);

    // 특정 파트너의 매장을 조회합니다.
    List<Store> findByUserId(Long userId);
}