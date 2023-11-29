package com.example.reservation.repository;

import com.example.reservation.domain.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r JOIN r.reservation res JOIN res.store s WHERE s.id = :storeId")
    List<Review> findByStoreId(@Param("storeId") Long storeId);

    @Query("SELECT r FROM Review r JOIN r.reservation res JOIN res.user s WHERE s.id = :userId")
    List<Review> findByUserId(@Param("userId") Long userId);
}
