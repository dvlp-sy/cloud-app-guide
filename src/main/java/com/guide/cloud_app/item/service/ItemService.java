package com.guide.cloud_app.item.service;

import com.guide.cloud_app.item.domain.ItemStatus;
import com.guide.cloud_app.item.domain.dto.ItemListResponse;
import com.guide.cloud_app.item.domain.dto.ItemRequest;
import com.guide.cloud_app.item.domain.dto.ItemResponse;
import com.guide.cloud_app.item.domain.dto.ItemStockResponse;
import com.guide.cloud_app.item.entity.Inventory;
import com.guide.cloud_app.item.entity.Item;
import com.guide.cloud_app.item.repository.InventoryRepository;
import com.guide.cloud_app.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    public ItemResponse registerItem(ItemRequest request) {
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid Quantity: " + request.getQuantity());
        }
        Item item = Item.of(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                ItemStatus.ON_SALE
        );

        item = itemRepository.save(item);

        Inventory inventory = Inventory.of(
                request.getQuantity(),
                request.getVersion(),
                item
        );

        inventoryRepository.save(inventory);

        return ItemResponse.of(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getStatus()
        );
    }

    public ItemListResponse getItemList() {
        return ItemListResponse.of(itemRepository.findAll().stream()
                .map(item -> ItemResponse.of(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getPrice(),
                        item.getStatus()
                ))
                .toList()
        );
    }

    public ItemStockResponse getItem(Long itemId) {
        Inventory inventory = inventoryRepository.findByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid itemId: " + itemId));
        Item item = inventory.getItem();
        return ItemStockResponse.of(
                itemId,
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                inventory.getQuantity()
        );
    }

    public ItemStockResponse addItemStock(Long itemId, Long quantity) {
        Inventory inventory = inventoryRepository.findByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid itemId: " + itemId));
        Item item = inventory.getItem();

        inventory.addQuantity(quantity);

        return ItemStockResponse.of(
                itemId,
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                inventory.getQuantity()
        );
    }
}
