package com.guide.cloud_app.item.exception;

import com.guide.cloud_app.common.api.SuccessResponse;
import org.springframework.http.HttpStatus;

public enum ItemSuccessResponse implements SuccessResponse {
    ITEM_REGISTERED(HttpStatus.CREATED, "ITEM2001", "상품 등록 완료"),
    ITEM_FOUND(HttpStatus.OK, "ITEM2002", "상품 조회 완료"),
    ITEM_LIST_FOUND(HttpStatus.OK, "ITEM2003", "상품 목록 조회 완료"),
    ITEM_STOCK_FOUND(HttpStatus.OK, "ITEM2004", "상품 재고 조회 완료"),
    ITEM_STOCK_UPDATED(HttpStatus.OK, "ITEM2005", "상품 재고 수정 완료"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ItemSuccessResponse(HttpStatus status, String code, String message) {
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
