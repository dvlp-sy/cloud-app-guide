package com.guide.cloud_app.order.dto;

import com.guide.cloud_app.member.domain.OrderStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderResponse {
    private final Long orderId;
    private final OrderStatus orderStatus;
    private final Long totalAmount;
    private final List<OrderItemResponse> items;

    private OrderResponse(Long orderId, OrderStatus orderStatus, Long totalAmount, List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    public static OrderResponse of(Long orderId, OrderStatus orderStatus,
                                   Long totalAmount,
                                   List<OrderItemResponse> items) {
        return new OrderResponse(orderId, orderStatus, totalAmount, items);
    }
}
