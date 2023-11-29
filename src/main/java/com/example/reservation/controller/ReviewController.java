package com.example.reservation.controller;

import com.example.reservation.dto.ReviewDto;
import com.example.reservation.dto.ReviewDto.Response;
import com.example.reservation.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping("/{reservationId}")
    public ResponseEntity<ReviewDto.Response> insertReview(@RequestBody ReviewDto.Request request, @PathVariable Long reservationId) {
        return ResponseEntity.ok(reviewService.insertReview(request, reservationId));
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto.Response> updateReview(@RequestBody ReviewDto.Request request, @PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.updateReview(request, reviewId));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDto.Response> deleteReview(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.deleteReview(reviewId));
    }

    // 매장의 리뷰 조회
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<Response>> getStoreReviews(@PathVariable Long storeId) {
        return ResponseEntity.ok(reviewService.getStoreReviews(storeId));
    }

    // 회원이 작성한 리뷰들 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Response>> getUserReviews(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getUserReviews(userId));
    }
}
