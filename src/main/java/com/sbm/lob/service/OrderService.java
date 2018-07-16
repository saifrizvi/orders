package com.sbm.lob.service;

import com.sbm.lob.domain.Order;
import com.sbm.lob.domain.OrderType;

import java.util.Map;

public interface OrderService {
    long register(Order order);

    void remove(Long orderId);

    Map<OrderType, Map<Double,Double>> aggregateSummary();
}
