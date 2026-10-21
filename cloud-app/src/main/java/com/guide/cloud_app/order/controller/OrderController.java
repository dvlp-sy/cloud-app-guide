package com.guide.cloud_app.order.controller;

import com.guide.cloud_app.order.dto.OrderListResponse;
import com.guide.cloud_app.order.dto.OrderResponse;
import com.guide.cloud_app.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders/{orderId}")
    public String orderDetail(
            @CookieValue(name = "memberId", required = false) Long memberId,
            @PathVariable Long orderId,
            Model model
    ) {
        OrderResponse order = orderService.getOrder(orderId);

        model.addAttribute("memberId", memberId);
        model.addAttribute("order", order);

        return "orders/detail";
    }

    @GetMapping("/orders")
    public String memberOrders(
            @CookieValue(name = "memberId", required = false) Long memberId,
            Model model
    ) {
       OrderListResponse orders = orderService.getOrderList(memberId);

        model.addAttribute("memberId", memberId);
        model.addAttribute("orders", orders.getOrders());

        return "orders/list";
    }
}
