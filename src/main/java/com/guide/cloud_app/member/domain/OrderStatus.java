package com.guide.cloud_app.member.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDERED("주문완료"),
    CANCELLED("주문취소"),
    DELIVERING("배송중"),
    COMPLETED("배송완료"),
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
