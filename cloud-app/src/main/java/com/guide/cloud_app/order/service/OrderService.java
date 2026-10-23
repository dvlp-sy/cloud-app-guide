package com.guide.cloud_app.order.service;

import com.guide.cloud_app.item.entity.Inventory;
import com.guide.cloud_app.item.repository.InventoryRepository;
import com.guide.cloud_app.member.domain.OrderStatus;
import com.guide.cloud_app.member.entity.Member;
import com.guide.cloud_app.order.dto.*;
import com.guide.cloud_app.order.entity.OrderItem;
import com.guide.cloud_app.order.entity.Orders;
import com.guide.cloud_app.order.repository.OrderItemRepository;
import com.guide.cloud_app.order.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final InventoryRepository inventoryRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderResponse orderItem(OrderRequest orderRequest, Member member) {
        List<OrderItemRequest> orderItemRequests = orderRequest.getItems();

        Orders orders = Orders.of(OrderStatus.ORDERED, 0L, member);
        orders = ordersRepository.save(orders);

        List<OrderItemResponse> orderItems = new ArrayList<>();

        for (OrderItemRequest order : orderItemRequests) {
            // 존재하는 아이템인지 확인
            Inventory inventory = inventoryRepository.findByItemId(order.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Item not found"));

            // 재고가 충분한지 확인
            if (inventory.getQuantity() < order.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for item: " + order.getItemId());
            }

            // 재고 차감
            inventory.reduceQuantity(order.getQuantity());

            // 주문 아이템 저장
            OrderItem orderItem = OrderItem.of(
                    order.getQuantity() * inventory.getItem().getPrice(),
                    order.getQuantity(),
                    order.getPriceSnapshot(),
                    orders,
                    inventory.getItem()
            );

            orders.addTotalAmount(orderItem.getAmount());
            orderItemRepository.save(orderItem);

            // 주문 아이템 응답 생성
            orderItems.add(OrderItemResponse.of(
                    orderItem.getItem().getId(),
                    orderItem.getItem().getName(),
                    orderItem.getItem().getDescription(),
                    orderItem.getAmount(),
                    orderItem.getQuantity()
            ));
        }

        return OrderResponse.of(
                orders.getId(),
                orders.getStatus(),
                orders.getTotalAmount(),
                orderItems
        );
    }

    public void cancelOrder(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.cancel();

        orderItemRepository.findAllByOrdersId(orderId).forEach(orderItem ->
                // 취소된 수량만큼 재고 업데이트
                inventoryRepository.findByItemId(orderItem.getItem().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Item not found"))
                    .addQuantity(orderItem.getQuantity())
        );

    }

    public OrderListResponse getOrderList(Long memberId) {
        List<Orders> orders = ordersRepository.findAllByMemberId(memberId);
        return OrderListResponse.of(orders.stream()
                // 주문 정보 (상태, 총 금액, 주문한 상품 정보) 반환
                .map(order -> OrderResponse.of(
                        order.getId(),
                        order.getStatus(),
                        order.getTotalAmount(),
                        orderItemRepository.findAllByOrdersId(order.getId()).stream()
                                .map(orderItem -> OrderItemResponse.of(
                                        orderItem.getItem().getId(),
                                        orderItem.getItem().getName(),
                                        orderItem.getItem().getDescription(),
                                        orderItem.getAmount(),
                                        orderItem.getQuantity()
                                ))
                                .toList()
                ))
                .toList());
    }

    public OrderResponse getOrder(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        List<OrderItem> orderItems = orderItemRepository.findAllByOrdersId(orderId);
        return OrderResponse.of(
                orderId,
                order.getStatus(),
                order.getTotalAmount(),
                orderItems.stream()
                        .map(orderItem -> OrderItemResponse.of(
                                orderItem.getItem().getId(),
                                orderItem.getItem().getName(),
                                orderItem.getItem().getDescription(),
                                orderItem.getAmount(),
                                orderItem.getQuantity()
                        ))
                        .toList()
        );
    }
}
