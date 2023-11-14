package com.example.reservation.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;   // 매장 식별키

    private String name;   // 매장 이름

    private String address;   // 상점 위치

    private String description;  // 상점 설명

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    public Store(String name, String address, String description) {
        this.name = name;
        this.address = address;
        this.description = description;
    }
}
