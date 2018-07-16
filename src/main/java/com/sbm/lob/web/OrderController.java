package com.sbm.lob.web;

import com.sbm.lob.domain.Order;
import com.sbm.lob.domain.OrderType;
import com.sbm.lob.exception.OrderInvalidException;
import com.sbm.lob.exception.OrderNotFoundException;
import com.sbm.lob.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/steel-bars-marketplace/api/v1")
public class OrderController {
    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    /*
    1. Register an Order -> Post (Create)
     */
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<Long> register(@RequestBody Order order){
        try{
            return new ResponseEntity(service.register(order), HttpStatus.CREATED);
        } catch (OrderInvalidException oe){
            return new ResponseEntity(-1l, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(-1l,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /*
    2. Cancel an Order -> Delete (Delete)
    #Note: Since the register api returns id of the order, we are assuming that the deletion api will delete order by id.
     */
    @RequestMapping(value = "/orders", method = RequestMethod.DELETE)
    public ResponseEntity<String> cancel(@RequestBody Long orderId){
        try{
            service.remove(orderId);
            return new ResponseEntity(orderId +" removed successfully.",HttpStatus.OK);
        } catch (OrderNotFoundException oe){
            return new ResponseEntity(orderId +" not found.",HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity("Server encountered an unexpected error.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    3. Summary of Live Orders -> Get (List)
     */
    @RequestMapping(value = "/orders/summary", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<OrderType, Map<Double, Double>>> summary(){
        try{
            return new ResponseEntity(service.aggregateSummary(),HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity("Server encountered an unexpected error.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}