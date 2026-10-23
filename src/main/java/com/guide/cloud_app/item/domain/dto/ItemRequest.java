package com.guide.cloud_app.item.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemRequest {
    private final String name;
    private final String description;
    private final Integer price;
    private final Long quantity;
    private final String version;
}
