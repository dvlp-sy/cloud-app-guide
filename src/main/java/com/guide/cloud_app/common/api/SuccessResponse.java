package com.guide.cloud_app.common.api;

import org.springframework.http.HttpStatus;

public interface SuccessResponse {
    HttpStatus getSuccessStatus();
    String getCode();
    String getMessage();
}
