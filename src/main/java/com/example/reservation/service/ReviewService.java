package com.example.reservation.service;

import com.example.reservation.domain.Reservation;
import com.example.reservation.domain.Review;
import com.example.reservation.domain.User;
import com.example.reservation.domain.constants.ReservationStatus;
import com.example.reservation.dto.ReviewDto;
import com.example.reservation.dto.ReviewDto.Request;
import com.example.reservation.dto.ReviewDto.Response;
import com.example.reservation.repository.ReservationRepository;
import com.example.reservation.repository.ReviewRepository;
import com.example.reservation.repository.UserRepository;
import com.example.reservation.security.SecurityUtil;
import com.example.reservation.security.TokenProvider;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    // 리뷰 등록
    @Transactional
    public Response insertReview(Request request, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 예약입니다."));

        // 리뷰 작성이 가능한 회원인지 검사
        if(!validateUser(reservation)) {
            User user = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow();

            throw new RuntimeException("예약자와 회원 정보가 일치하지 않거나 예약이 완료되지 않았습니다. 예약자는 " + reservation.getUser().getUsername() + "이고, 현재 회원은 " +
                user.getUsername() + " 입니다.");
        }
        Review review = request.toEntity(request, reservation);
        reviewRepository.save(review);

        return ReviewDto.fromEntity(review);
    }

    // 리뷰 수정
    @Transactional
    public Response updateReview(Request request, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 리뷰입니다."));
        Reservation reservation = review.getReservation();

        if(!validateUser(reservation)) {
            throw new RuntimeException("예약자와 회원 정보가 일치하지 않습니다.");
        }

        review.update(request.getContent());
        reviewRepository.save(review);

        return ReviewDto.fromEntity(review);
    }

    // 리뷰 삭제
    @Transactional
    public Response deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 리뷰입니다."));
        reviewRepository.deleteById(reviewId);

        return ReviewDto.fromEntity(review);
    }

    public List<Response> getStoreReviews(Long storeId) {
        return reviewRepository.findByStoreId(storeId).stream()
            .map(ReviewDto::fromEntity)
            .collect(Collectors.toList());
    }

    public List<Response> getUserReviews(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
            .map(ReviewDto::fromEntity)
            .collect(Collectors.toList());
    }

    // 회원 검증
    private boolean validateUser(Reservation reservation) {
        User user = reservation.getUser();
        User curUser = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        // 예약자와 로그인 유저가 동일하지 않다면.
        if(!user.getUsername().equals(curUser.getUsername())) {
            return false;  // false 를 반환
        }

        return reservation.getStatus() == ReservationStatus.END;  // 예약과 도착을 완료한 상태여야 리뷰 작성 가능
    }


}
