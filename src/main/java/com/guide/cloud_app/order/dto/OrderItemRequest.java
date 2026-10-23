package com.guide.cloud_app.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemRequest {
    private final Long itemId;
    private final Long quantity;
    private final Long priceSnapshot;
}
