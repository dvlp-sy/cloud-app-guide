package com.guide.cloud_app.item.controller;

import com.guide.cloud_app.common.api.ApiResponse;
import com.guide.cloud_app.item.domain.dto.*;
import com.guide.cloud_app.item.api.ItemSuccessResponse;
import com.guide.cloud_app.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemApiController implements ItemApiControllerDocs {

    private final ItemService itemService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponse<ItemResponse>> registerItem(
            @RequestBody ItemRequest request) {
        return ApiResponse.onSuccess(ItemSuccessResponse.ITEM_REGISTERED,
                itemService.registerItem(request));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponse<ItemListResponse>> getItemList() {
        return ApiResponse.onSuccess(ItemSuccessResponse.ITEM_LIST_FOUND, itemService.getItemList());
    }

    @Override
    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItemStockResponse>> getItem(@PathVariable Long itemId) {
        return ApiResponse.onSuccess(ItemSuccessResponse.ITEM_FOUND, itemService.getItem(itemId));
    }

    @Override
    @PostMapping("/{itemId}")
    public ResponseEntity<ApiResponse<ItemStockResponse>> addItemStock(
            @PathVariable Long itemId,
            @RequestParam Long quantity) {
        return ApiResponse.onSuccess(ItemSuccessResponse.ITEM_STOCK_UPDATED,
                itemService.addItemStock(itemId, quantity));
    }
}
