package com.guide.cloud_app.item.domain.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemListResponse {
    private final List<ItemResponse> items;

    private ItemListResponse(List<ItemResponse> items) {
        this.items = items;
    }

    public static ItemListResponse of(List<ItemResponse> items) {
        return new ItemListResponse(items);
    }
}
