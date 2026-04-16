package com.grocery.model;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String itemNumber;
    private String category;
    private Double temperature;
    private String location;
    private Integer quantity;
    private Double price;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getItemNumber() { return itemNumber; }
    public void setItemNumber(String itemNumber) { this.itemNumber = itemNumber; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
