package com.example.reservation.repository;

import com.example.reservation.domain.Reservation;
import com.example.reservation.domain.Store;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByReservationTimeAndStore(LocalDateTime reservationTime, Store store);
}
