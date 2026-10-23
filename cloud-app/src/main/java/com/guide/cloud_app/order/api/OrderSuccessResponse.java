package com.guide.cloud_app.order.api;

import com.guide.cloud_app.common.api.SuccessResponse;
import org.springframework.http.HttpStatus;

public enum OrderSuccessResponse implements SuccessResponse {
    ORDER_CREATED(HttpStatus.CREATED, "ORDER2001", "주문 완료"),
    ORDER_CANCELLED(HttpStatus.OK, "ORDER2002", "주문 취소"),
    ORDER_LIST_FOUND(HttpStatus.OK, "ORDER2003", "주문 목록 조회 완료"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    OrderSuccessResponse(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getSuccessStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
