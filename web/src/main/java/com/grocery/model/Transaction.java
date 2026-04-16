package com.grocery.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerUsername;
    private Double total;
    private LocalDateTime transactionDate;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionItem> items = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerUsername() { return customerUsername; }
    public void setCustomerUsername(String customerUsername) { this.customerUsername = customerUsername; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public List<TransactionItem> getItems() { return items; }
    public void setItems(List<TransactionItem> items) { this.items = items; }
}
