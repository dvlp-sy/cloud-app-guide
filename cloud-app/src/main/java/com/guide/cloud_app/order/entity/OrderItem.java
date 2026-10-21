package com.guide.cloud_app.order.entity;

import com.guide.cloud_app.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long priceSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public static OrderItem of(Long amount, Long quantity, Long priceSnapshot, Orders orders, Item item) {
        return OrderItem.builder()
                .amount(amount)
                .quantity(quantity)
                .priceSnapshot(priceSnapshot)
                .orders(orders)
                .item(item)
                .build();
    }
}
