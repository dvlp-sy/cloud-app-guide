package com.guide.cloud_app.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @GetMapping("/items")
    public String itemList(Model model) {
        // TODO: REST API를 호출하여 상품 목록 조회
        // model.addAttribute("items", itemService.findAll());

        model.addAttribute("items", List.of(
                Map.of(
                        "id", 1,
                        "name", "Wireless Keyboard",
                        "price", 59000,
                        "description", "무선 키보드"
                ),
                Map.of(
                        "id", 2,
                        "name", "Wireless Mouse",
                        "price", 35000,
                        "description", "무선 마우스"
                ),
                Map.of(
                        "id", 3,
                        "name", "USB-C Hub",
                        "price", 42000,
                        "description", "USB-C 멀티 허브"
                )
        ));

        return "items/list";
    }

    @GetMapping("/items/{itemId}")
    public String itemDetail(@PathVariable Long itemId, Model model) {
        // TODO: REST API를 호출하여 상품 상세 조회
        // model.addAttribute("item", itemService.findById(itemId));

        model.addAttribute("item", Map.of(
                "id", itemId,
                "name", "Wireless Keyboard",
                "price", 59000,
                "description", "편안한 타이핑을 위한 무선 키보드입니다.",
                "stock", 25
        ));

        return "items/detail";
    }
}
