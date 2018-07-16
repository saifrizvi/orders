package com.sbm.lob.repository;

import com.sbm.lob.domain.Order;
import com.sbm.lob.domain.OrderType;
import com.sbm.lob.exception.OrderNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderRepositoryImplTest {

    private OrderRepository repository;
    private Order testOrder;

    @Before
    public void setUp() throws Exception {
        repository = new OrderRepositoryImpl();

    }

    @Test
    public void should_save_valid_order() throws Exception {
        //given
        testOrder = new Order("testUser", 2, 500, OrderType.SELL);

        //when
        repository.save(testOrder);

        //then
        assertEquals(1, repository.getAll()
                .stream()
                .filter(order -> order.getUserId().equalsIgnoreCase("testUser"))
                .count());

    }

    @Test
    public void should_remove_existing_order() throws Exception {
        //given
        testOrder = new Order("testUser", 2, 500, OrderType.SELL);
        long orderId = repository.save(testOrder);

        //when
        repository.remove(orderId);


        //then
        assertEquals(0, repository.getAll()
                .stream()
                .filter(order -> order.getUserId().equalsIgnoreCase("testUser"))
                .count());
    }

    @Test(expected = OrderNotFoundException.class)
    public void should_error_for_non_existing_order() throws Exception {
        //given
        long orderId = 10l;

        //when //then
        repository.remove(orderId);
    }

    @After
    public void tearDown() throws Exception {
        repository = null;

    }

}