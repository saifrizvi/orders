package com.sbm.lob.domain;

public class Order {
    private final String userId;
    private final double quantity;
    private final double price;
    private final OrderType type;


    public Order(String userId, double quantity, double price, OrderType type) {
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public OrderType getType() {
        return type;
    }
}
