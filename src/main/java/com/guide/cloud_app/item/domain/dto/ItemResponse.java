package com.guide.cloud_app.item.domain.dto;

import com.guide.cloud_app.item.domain.ItemStatus;
import lombok.Getter;

@Getter
public class ItemResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final Integer price;
    private final ItemStatus status;

    private ItemResponse(Long id, String name, String description, Integer price, ItemStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
    }

    public static ItemResponse of(Long id, String name, String description, Integer price, ItemStatus status) {
        return new ItemResponse(id, name, description, price, status);
    }
}
