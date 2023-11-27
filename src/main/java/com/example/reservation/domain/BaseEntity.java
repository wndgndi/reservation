package com.example.reservation.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    private String createdDate;   // 생성 날짜

    @LastModifiedDate
    private String updatedDate;   // 수정 날짜

    @PrePersist  // 지정된 포맷으로 날짜를 저장 (ex: '2023년 11월 24일 (금) 16시 26분 27초')
    public void onPrePersist() {
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 (E) HH시 mm분 ss초"));
        this.updatedDate = createdDate;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 (E) HH시 mm분 ss초"));
    }

}
