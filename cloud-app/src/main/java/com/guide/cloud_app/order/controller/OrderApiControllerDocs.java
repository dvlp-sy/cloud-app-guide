package com.guide.cloud_app.order.controller;

import com.guide.cloud_app.common.api.ApiResponse;
import com.guide.cloud_app.order.dto.OrderListResponse;
import com.guide.cloud_app.order.dto.OrderRequest;
import com.guide.cloud_app.order.dto.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "주문", description = "주문 관련 API")
public interface OrderApiControllerDocs {

    // 상품 주문
    @Operation(summary = "상품 주문", description = "상품을 주문합니다.")
    ResponseEntity<ApiResponse<OrderResponse>> orderItem(Long memberId, OrderRequest request);

    // 상품 주문 취소
    @Operation(summary = "상품 주문 취소", description = "주문한 상품을 취소합니다.")
    ResponseEntity<ApiResponse<Void>> cancelOrder(Long memberId, Long orderId);

    // 주문 목록 조회
    @Operation(summary = "주문 목록 조회", description = "사용자의 주문 목록을 조회합니다.")
    ResponseEntity<ApiResponse<OrderListResponse>> getOrderList(Long memberId);
}
