package com.guide.cloud_app.common.api;

import org.springframework.http.HttpStatus;

public interface ErrorResponse {
    HttpStatus getErrorStatus();
    String getCode();
    String getMessage();
}