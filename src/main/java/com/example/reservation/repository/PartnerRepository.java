package com.example.reservation.repository;

import com.example.reservation.domain.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    boolean existsByUsername(String username);
}
