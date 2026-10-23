package com.guide.cloud_app.member.controller;

import com.guide.cloud_app.common.api.ApiResponse;
import com.guide.cloud_app.member.api.MemberSuccessResponse;
import com.guide.cloud_app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController implements MemberApiControllerDocs {

    private final MemberService memberService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<String>> signUp(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String password) {
        memberService.signup(email, name, password);
        return ApiResponse.onSuccess(MemberSuccessResponse.MEMBER_CREATED, email);
    }
}
