package com.sbm.lob.repository;

import com.sbm.lob.domain.Order;

import java.util.List;

public interface OrderRepository {
    long save(Order order);

    List<Order> getAll();

    void remove(Long orderId);
}
