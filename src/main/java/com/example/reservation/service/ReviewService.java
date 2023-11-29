package com.example.reservation.service;

import com.example.reservation.domain.Reservation;
import com.example.reservation.domain.Review;
import com.example.reservation.domain.User;
import com.example.reservation.domain.constants.ErrorCode;
import com.example.reservation.domain.constants.ReservationStatus;
import com.example.reservation.dto.ReviewDto;
import com.example.reservation.dto.ReviewDto.Request;
import com.example.reservation.dto.ReviewDto.Response;
import com.example.reservation.exception.CustomException;
import com.example.reservation.repository.ReservationRepository;
import com.example.reservation.repository.ReviewRepository;
import com.example.reservation.repository.UserRepository;
import com.example.reservation.security.SecurityUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        // 리뷰 작성이 가능한 회원인지 검사
        validateUser(reservation);

        Review review = request.toEntity(request, reservation);
        reviewRepository.save(review);

        return ReviewDto.fromEntity(review);
    }

    // 리뷰 수정
    @Transactional
    public Response updateReview(Request request, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        Reservation reservation = review.getReservation();

        validateUser(reservation);

        review.update(request.getContent());
        reviewRepository.save(review);

        return ReviewDto.fromEntity(review);
    }

    // 리뷰 삭제
    @Transactional
    public Response deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
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
    // 예약자가 정상적으로 도착을 완료한 상태여야 리뷰 작성 가능
    private void validateUser(Reservation reservation) {
        User user = reservation.getUser();
        User curUser = userRepository.findById(SecurityUtil.getCurrentUserId())
            .orElseThrow(() -> new CustomException(
                ErrorCode.USER_NOT_FOUND));

        // 예약자와 로그인 유저가 동일하지 않다면.
        if (!user.getUsername().equals(curUser.getUsername())) {
            throw new CustomException(ErrorCode.USER_MISMATCH);
        }

        // 예약자가 도착 완료한 상태가 아니라면,
        if (reservation.getStatus() != ReservationStatus.END) {
            throw new CustomException(ErrorCode.USER_NOT_ARRIVE);
        }
    }

}
