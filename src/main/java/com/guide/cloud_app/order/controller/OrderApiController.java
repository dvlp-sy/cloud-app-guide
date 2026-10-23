package com.guide.cloud_app.order.controller;

import com.guide.cloud_app.common.api.ApiResponse;
import com.guide.cloud_app.member.entity.Member;
import com.guide.cloud_app.member.service.MemberService;
import com.guide.cloud_app.order.api.OrderSuccessResponse;
import com.guide.cloud_app.order.dto.OrderListResponse;
import com.guide.cloud_app.order.dto.OrderRequest;
import com.guide.cloud_app.order.dto.OrderResponse;
import com.guide.cloud_app.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderApiController implements OrderApiControllerDocs {

    private final MemberService memberService;
    private final OrderService orderService;


    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> orderItem(
            @CookieValue(name = "memberId") Long memberId,
            @RequestBody OrderRequest request
    ) {
        Member member = memberService.getMember(memberId);
        return ApiResponse.onSuccess(OrderSuccessResponse.ORDER_CREATED,
                orderService.orderItem(request, member));
    }

    @Override
    @PatchMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @CookieValue(name = "memberId") Long memberId,
            @PathVariable Long orderId) {
        memberService.validateMember(memberId);
        orderService.cancelOrder(orderId);
        return ApiResponse.onSuccess(OrderSuccessResponse.ORDER_CANCELLED);
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<OrderListResponse>> getOrderList(
            @CookieValue(name = "memberId") Long memberId
    ) {
        memberService.validateMember(memberId);
        return ApiResponse.onSuccess(OrderSuccessResponse.ORDER_LIST_FOUND,
                orderService.getOrderList(memberId));
    }
}
