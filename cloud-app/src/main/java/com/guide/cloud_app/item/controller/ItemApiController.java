package com.guide.cloud_app.item.controller;

import com.guide.cloud_app.common.api.ApiResponse;
import com.guide.cloud_app.item.domain.ItemStatus;
import com.guide.cloud_app.item.domain.dto.*;
import com.guide.cloud_app.item.exception.ItemSuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemApiController implements ItemApiControllerDocs {

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<ItemResponse>> registerItem(
            @RequestBody ItemRequest request) {
        return ApiResponse.onSuccess(ItemSuccessResponse.ITEM_REGISTERED, null);
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<ItemListResponse>> getItemList() {
        return ApiResponse.onSuccess(ItemSuccessResponse.ITEM_LIST_FOUND, ItemListResponse.of(
                List.of(
                        ItemResponse.of(1L, "Wireless Keyboard", "무선 키보드", 59000, ItemStatus.ON_SALE),
                        ItemResponse.of(2L, "Wireless Mouse", "무선 마우스", 35000, ItemStatus.ON_SALE),
                        ItemResponse.of(3L, "USB-C Hub", "USB-C 멀티 허브", 42000, ItemStatus.SOLD_OUT),
                        ItemResponse.of(4L, "USB-C Hub", "USB-C 멀티 허브", 42000, ItemStatus.SOLD_OUT)
                )
        ));
    }

    @Override
    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItemResponse>> getItem(@PathVariable Long itemId) {
        return ApiResponse.onSuccess(ItemSuccessResponse.ITEM_FOUND, null);
    }

    @Override
    @GetMapping("/{itemId}/stock")
    public ResponseEntity<ApiResponse<ItemStockResponse>> getItemStock(@PathVariable Long itemId) {
        return ApiResponse.onSuccess(ItemSuccessResponse.ITEM_STOCK_FOUND, null);
    }

    @Override
    @PostMapping("/{itemId}/stocks/{quantity}")
    public ResponseEntity<ApiResponse<ItemStockResponse>> updateItemStock(
            @RequestBody ItemStockRequest request) {
        return ApiResponse.onSuccess(ItemSuccessResponse.ITEM_STOCK_UPDATED, null);
    }
}
