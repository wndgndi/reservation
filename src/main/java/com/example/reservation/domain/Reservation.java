package com.example.reservation.domain;

import com.example.reservation.domain.constants.ReservationStatus;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime reservationTime;

    private LocalDateTime arrivedAt;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne(mappedBy = "reservation")
    private Review review;

    public Reservation(LocalDateTime reservationTime, User user, Store store) {
        this.reservationTime = reservationTime;
        this.status = ReservationStatus.PENDING;
        this.user = user;
        this.store = store;
    }

    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }

    public void arrive(ReservationStatus status) {
        this.arrivedAt = LocalDateTime.now();
        this.status = status;
    }
}
