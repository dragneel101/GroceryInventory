package com.grocery.model;

import java.io.Serializable;

public class CartItem implements Serializable {

    private Long itemId;
    private String description;
    private Double price;
    private Integer quantity;

    public CartItem() {}

    public CartItem(Long itemId, String description, Double price, Integer quantity) {
        this.itemId = itemId;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public double getSubtotal() { return price * quantity; }
}
