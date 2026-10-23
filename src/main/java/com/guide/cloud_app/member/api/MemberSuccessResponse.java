package com.guide.cloud_app.member.api;

import com.guide.cloud_app.common.api.SuccessResponse;
import org.springframework.http.HttpStatus;

public enum MemberSuccessResponse implements SuccessResponse {
    MEMBER_CREATED(HttpStatus.CREATED, "MEMBER2001", "회원 등록 완료"),

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    MemberSuccessResponse(HttpStatus status, String code, String message) {
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
