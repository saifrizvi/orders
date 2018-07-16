package com.sbm.lob.service;

import com.sbm.lob.domain.Order;
import com.sbm.lob.domain.OrderType;
import com.sbm.lob.exception.OrderInvalidException;
import com.sbm.lob.repository.OrderRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {
    public static final long TEST_ID = 10l;
    private OrderService service;
    private Order testOrder;
    private Order invalidOrder;

    @Mock
    private OrderRepository repository;

    @Before
    public void setUp() throws Exception {
        service = new OrderServiceImpl(repository);
        testOrder = new Order("testUser", 2, 500, OrderType.SELL);
    }

    @Test
    public void should_register_valid_order() throws Exception {
        //given
        when(repository.save(testOrder)).thenReturn(TEST_ID);

        //when
        long id = service.register(testOrder);

        //then
        assertEquals(TEST_ID, id);
    }

    @Test(expected = OrderInvalidException.class)
    public void should_error_for_invalid_order() throws Exception {
        //given
        invalidOrder = new Order("", -1, 500, OrderType.SELL);

        //when then
        service.register(invalidOrder);
    }

    @Test
    public void should_remove_existing_order() throws Exception {
        //given
        //when
        service.remove(TEST_ID);

        //then
        verify(repository, times(1)).remove(TEST_ID);
    }

    @Test
    public void should_calculate_summary_for_valid_orders(){
        //given
        List<Order> testOrders = new ArrayList<>();
        testOrders.add(new Order("testUser1", 3.5, 306, OrderType.SELL));
        testOrders.add(new Order("testUser2", 1.2, 310, OrderType.SELL));
        testOrders.add(new Order("testUser3", 1.5, 307, OrderType.SELL));
        testOrders.add(new Order("testUser4", 2.0, 306, OrderType.SELL));
        testOrders.add(new Order("testUser1", 3.5, 306, OrderType.BUY));
        testOrders.add(new Order("testUser2", 1.2, 310, OrderType.BUY));
        testOrders.add(new Order("testUser3", 1.5, 307, OrderType.BUY));
        testOrders.add(new Order("testUser4", 2.0, 306, OrderType.BUY));
        when(repository.getAll()).thenReturn(testOrders);

        //when
        Map<OrderType, Map<Double, Double>> orderTypePriceQuantity = service.aggregateSummary();

        //then
        assertNotNull(orderTypePriceQuantity);
        assertEquals(2, orderTypePriceQuantity.size());
        assertEquals(3, orderTypePriceQuantity.get(OrderType.SELL).size());
        assertEquals(Double.valueOf(5.5), orderTypePriceQuantity.get(OrderType.SELL).get(Double.valueOf(306)));
        assertEquals(3, orderTypePriceQuantity.get(OrderType.BUY).size());

    }

    @After
    public void tearDown() throws Exception {
        service = null;
    }
}