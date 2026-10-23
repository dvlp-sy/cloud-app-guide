package com.guide.cloud_app.member.controller;

import com.guide.cloud_app.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원", description = "회원 관련 API")
public interface MemberApiControllerDocs {

    // 회원가입
    @Operation(summary = "[테스트용] 회원가입", description = "DB에 회원을 추가합니다.")
    ResponseEntity<ApiResponse<String>> signUp(String email, String name, String password);
}
