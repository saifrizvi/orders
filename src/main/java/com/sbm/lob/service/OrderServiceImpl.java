package com.sbm.lob.service;

import com.sbm.lob.domain.Order;
import com.sbm.lob.domain.OrderType;
import com.sbm.lob.exception.OrderInvalidException;
import com.sbm.lob.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repository;

    @Autowired
    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    public long register(Order order) {
        if (null == order || StringUtils.isEmpty(order.getUserId()) || order.getPrice() < 0 || order.getQuantity() < 0) {
            throw new OrderInvalidException("Invalid order request.");
        }
        return repository.save(order);
    }

    @Override
    public void remove(Long orderId) {
        repository.remove(orderId);
    }

    @Override
    public Map<OrderType, Map<Double, Double>> aggregateSummary() {
        Map<OrderType, Map<Double, Double>> response = new LinkedHashMap<>();

        List<Order> orders = repository.getAll();
        if (null != orders && orders.size() > 0) {
            //Map by Sell & Buy
            Map<OrderType, List<Order>> ordersByType = orders.stream()
                    .collect(Collectors.groupingBy(Order::getType, Collectors.toList()));

            if (null != ordersByType && ordersByType.containsKey(OrderType.SELL)) {
                //Group Sell by Price
                Map<Double, Double> priceQuantityForSell = ordersByType.get(OrderType.SELL).stream()
                        .collect(Collectors.groupingBy(Order::getPrice, Collectors.summingDouble(Order::getQuantity)));

                //Sort Sell
                LinkedHashMap<Double, Double> sellOrdersSorted = priceQuantityForSell.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

                response.put(OrderType.SELL, sellOrdersSorted);
            }

            if (null != ordersByType && ordersByType.containsKey(OrderType.BUY)) {
                //Group Buy by Price
                Map<Double, Double> priceQuantityForBuy = ordersByType.get(OrderType.BUY).stream()
                        .collect(Collectors.groupingBy(Order::getPrice, Collectors.summingDouble(Order::getQuantity)));

                //Sort Buy
                LinkedHashMap<Double, Double> buyOrdersSorted = priceQuantityForBuy.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

                response.put(OrderType.BUY, buyOrdersSorted);
            }
        }

        return response;
    }
}
