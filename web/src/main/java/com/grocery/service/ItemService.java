package com.grocery.service;

import com.grocery.model.Item;
import com.grocery.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item findById(Long id) {
        return itemRepository.findById(Objects.requireNonNull(id))
            .orElseThrow(() -> new RuntimeException("Item not found: " + id));
    }

    public Item save(Item item) {
        return itemRepository.save(Objects.requireNonNull(item));
    }

    public void delete(Long id) {
        itemRepository.deleteById(Objects.requireNonNull(id));
    }

    public List<Item> search(String type, String query) {
        return switch (type) {
            case "name"     -> itemRepository.findByDescriptionContainingIgnoreCase(query);
            case "category" -> itemRepository.findByCategoryContainingIgnoreCase(query);
            case "price"    -> {
                try {
                    yield itemRepository.findByPriceLessThanEqual(Double.parseDouble(query));
                } catch (NumberFormatException e) {
                    yield List.of();
                }
            }
            default -> itemRepository.findAll();
        };
    }

    public long countAll() {
        return itemRepository.count();
    }

    public double totalInventoryValue() {
        return itemRepository.findAll().stream()
            .mapToDouble(i -> i.getPrice() * i.getQuantity())
            .sum();
    }

    public List<Item> getLowStockItems(int threshold) {
        return itemRepository.findByQuantityLessThan(threshold);
    }
}
