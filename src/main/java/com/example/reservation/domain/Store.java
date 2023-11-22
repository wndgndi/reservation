package com.example.reservation.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Store extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;   // 매장 식별키

    private String name;   // 매장 이름

    private String address;   // 상점 위치

    private String description;  // 상점 설명

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Store(String name, String address, String description, User user) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.user = user;
    }

    public void updateStore(String name, String address, String description) {
        this.name = name;
        this.address = address;
        this.description = description;
    }
}
