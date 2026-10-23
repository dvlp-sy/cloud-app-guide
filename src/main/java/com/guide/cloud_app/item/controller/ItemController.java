package com.guide.cloud_app.item.controller;

import com.guide.cloud_app.item.domain.dto.ItemStockResponse;
import com.guide.cloud_app.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items")
    public String itemList() {
        return "items/list";
    }

    @GetMapping("/items/{itemId}")
    public String itemDetail(@PathVariable Long itemId, Model model) {
        model.addAttribute("item", itemService.getItem(itemId));
        return "items/detail";
    }
}
