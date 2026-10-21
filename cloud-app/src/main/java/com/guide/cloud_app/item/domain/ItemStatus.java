package com.guide.cloud_app.item.domain;

import lombok.Getter;

@Getter
public enum ItemStatus {
    SOLD_OUT("품절"),
    ON_SALE("판매 중"),
    ;

    private final String description;

    ItemStatus(String description) {
        this.description = description;
    }
}
