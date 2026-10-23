package com.guide.cloud_app.member.controller;

import com.guide.cloud_app.member.entity.Member;
import com.guide.cloud_app.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String home(@CookieValue(name = "memberId", required = false) String memberId) {
        if (memberId == null) {
            return "redirect:/login";
        }

        try {
            memberService.validateMember(Long.valueOf(memberId));
        } catch (IllegalArgumentException e) {
            return "redirect:/login";
        }

        return "redirect:/items";

    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        Member member;
        try {
            member = memberService.login(email, password);
        } catch (IllegalArgumentException e) {
            return "redirect:/login";
        }

        Cookie cookie = new Cookie("memberId", String.valueOf(member.getId()));
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setSecure(false);
        response.addCookie(cookie);

        return "redirect:/items";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("memberId", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/login";
    }
}
