package com.guide.cloud_app.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final boolean isSuccess;            // 성공 여부
    private final String code;                  // 응답 코드
    private final String message;               // 응답 메시지

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;                             // 응답 데이터

    public ApiResponse(boolean isSuccess, String code, String message, T data) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResponseEntity<ApiResponse<Void>> onSuccess(SuccessResponse status) {
        return ResponseEntity
                .status(status.getSuccessStatus().value())
                .body(new ApiResponse<>(status));
    }

    public static <T> ResponseEntity<ApiResponse<T>> onSuccess(SuccessResponse status, T data) {
        return ResponseEntity
                .status(status.getSuccessStatus().value())
                .body(new ApiResponse<>(true, status.getCode(), status.getMessage(), data));

    }

    public static ResponseEntity<ApiResponse<Void>> onFailure(ErrorResponse status) {
        return ResponseEntity
                .status(status.getErrorStatus().value())
                .body(new ApiResponse<>(status));
    }

    public static <T> ResponseEntity<ApiResponse<T>> onFailure(ErrorResponse status, T data) {
        return ResponseEntity
                .status(status.getErrorStatus().value())
                .body(new ApiResponse<>(false, status.getCode(), status.getMessage(), data));
    }

    private ApiResponse(SuccessResponse response) {
        this.isSuccess = true;
        this.code = response.getCode();
        this.message = response.getMessage();
    }

    private ApiResponse(ErrorResponse response) {
        this.isSuccess = false;
        this.code = response.getCode();
        this.message = response.getMessage();
    }
}
