package com.guide.cloud_app.order.entity;

import com.guide.cloud_app.member.domain.OrderStatus;
import com.guide.cloud_app.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder(access = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Long totalAmount;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Orders of(OrderStatus status, Long totalAmount, Member member) {
        return Orders.builder()
                .status(status)
                .totalAmount(totalAmount)
                .member(member)
                .build();
    }

    public void addTotalAmount(Long amount) {
        totalAmount += amount;
    }

    public void cancel() {
        if (!OrderStatus.ORDERED.equals(status)) {
            throw new IllegalArgumentException("Order status must be ORDERED");
        }
        status = OrderStatus.CANCELLED;
    }
}
