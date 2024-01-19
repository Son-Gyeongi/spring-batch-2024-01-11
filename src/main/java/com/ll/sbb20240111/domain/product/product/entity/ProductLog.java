package com.ll.sbb20240111.domain.product.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public class ProductLog { // 로그 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product product;

    private String name;

    /*
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))

    @JoinColumn() : JPA(Java Persistence API)에서 엔터티 간의 관계를 맺을 때 사용되는 어노테이션
    @ForeignKey(ConstraintMode.NO_CONSTRAINT) : 외래 키 제약 조건(Constraint)을 설정하는 부분
    ConstraintMode.NO_CONSTRAINT : 외래 키 제약 조건을 적용하지 않겠다는 것을 의미

    즉, 외래 키 제약을 두지 않고, 해당 엔터티 간의 관계를 자유롭게 설정
    테스트 용도로 사용하기 위해 외래 키를 제약하지 않을 수 있습니다.
     */
}
