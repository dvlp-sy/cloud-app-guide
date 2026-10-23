package com.guide.cloud_app.order.dto;

import lombok.Getter;

@Getter
public class OrderItemResponse {
    private final Long itemId;
    private final String name;
    private final String description;
    private final Long amount;
    private final Long quantity;

    private OrderItemResponse(Long itemId, String name, String description,
                              Long amount, Long quantity) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.quantity = quantity;
    }

    public static OrderItemResponse of(Long itemId, String name, String description,
                                       Long amount, Long quantity) {
        return new OrderItemResponse(itemId, name, description, amount, quantity);
    }
}
