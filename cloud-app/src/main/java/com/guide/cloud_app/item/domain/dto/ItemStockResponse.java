package com.guide.cloud_app.item.domain.dto;

import lombok.Getter;

@Getter
public class ItemStockResponse {
    private final Long itemId;
    private final String name;
    private final String description;
    private final Integer price;
    private final Long quantity;

    private ItemStockResponse(Long itemId, String name, String description, Integer price, Long quantity) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public static ItemStockResponse of(Long itemId, String name, String description, Integer price, Long quantity) {
        return new ItemStockResponse(itemId, name, description, price, quantity);
    }
}
