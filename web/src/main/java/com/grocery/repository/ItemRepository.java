package com.grocery.repository;

import com.grocery.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByDescriptionContainingIgnoreCase(String description);
    List<Item> findByCategoryContainingIgnoreCase(String category);
    List<Item> findByPriceLessThanEqual(Double price);
    List<Item> findByQuantityLessThan(Integer quantity);
}
