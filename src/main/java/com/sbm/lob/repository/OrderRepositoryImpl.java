package com.sbm.lob.repository;

import com.sbm.lob.domain.Order;
import com.sbm.lob.exception.OrderNotFoundException;
import com.sbm.lob.utils.Generator;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository{
    private final Map<Long, Order> liveOrders;

    public OrderRepositoryImpl() {
        this.liveOrders = new ConcurrentHashMap<>();
    }

    @Override
    public long save(Order order) {
        long id = Generator.getId();
        liveOrders.put(id, order);
        return id;
    }

    @Override
    public void remove(Long orderId) {
        if(!liveOrders.containsKey(orderId)){
            throw new OrderNotFoundException("OrderId "+orderId+" cound not be found.");
        } else{
            liveOrders.remove(orderId);
        }
    }

    @Override
    public List<Order> getAll() {
        return liveOrders.values()
                        .stream()
                        .collect(Collectors.toList());
    }
}