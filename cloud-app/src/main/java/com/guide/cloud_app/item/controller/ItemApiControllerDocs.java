package com.guide.cloud_app.item.controller;

import com.guide.cloud_app.common.api.ApiResponse;
import com.guide.cloud_app.item.domain.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "상품", description = "상품 관련 API")
public interface ItemApiControllerDocs {

    // 상품 등록
    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다.")
    ResponseEntity<ApiResponse<ItemResponse>> registerItem(ItemRequest request);

    // 상품 목록 조회
    @Operation(summary = "상품 목록 조회", description = "등록된 상품 목록을 조회합니다.")
    ResponseEntity<ApiResponse<ItemListResponse>> getItemList();

    // 상품 상세 조회
    @Operation(summary = "상품 상세 조회", description = "상품의 상세 정보를 조회합니다.")
    ResponseEntity<ApiResponse<ItemResponse>> getItem(Long id);

    // 상품 재고 조회
    @Operation(summary = "상품 재고 조회", description = "상품의 재고 정보를 조회합니다.")
    ResponseEntity<ApiResponse<ItemStockResponse>> getItemStock(Long id);

    // 상품 재고 수정
    @Operation(summary = "상품 재고 수정", description = "상품의 재고 정보를 수정합니다.")
    ResponseEntity<ApiResponse<ItemStockResponse>> updateItemStock(ItemStockRequest request);
}
