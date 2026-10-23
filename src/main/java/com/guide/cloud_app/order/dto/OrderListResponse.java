package com.guide.cloud_app.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderListResponse {
    private final List<OrderResponse> orders;

    private OrderListResponse(List<OrderResponse> orders) {
        this.orders = orders;
    }

    public static OrderListResponse of(List<OrderResponse> orders) {
        return new OrderListResponse(orders);
    }
}
