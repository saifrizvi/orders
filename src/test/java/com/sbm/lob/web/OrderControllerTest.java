package com.sbm.lob.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbm.lob.domain.Order;
import com.sbm.lob.domain.OrderType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MockMvc mvc;

    private Order testOrder;

    @Before
    public void setUp() {
        testOrder = new Order("testUser", 2, 500, OrderType.SELL);
    }

    @Test
    public void register_should_return_valid_response_code() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/steel-bars-marketplace/api/v1/orders")
                        .content(convertObjectToJsonBytes(testOrder))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void cancel_should_return_not_found_for_non_exsistent_id() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .delete("/steel-bars-marketplace/api/v1/orders")
                .content(convertObjectToJsonBytes(Long.valueOf(10)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void get_summary_should_return_valid_response_code() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/steel-bars-marketplace/api/v1/orders/summary")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                //.andExpect(content().string(equalTo("[{\"id\":0,\"userId\":\"user1\",\"quantity\":5.0,\"price\":305.0,\"type\":\"BUY\"},{\"id\":0,\"userId\":\"user2\",\"quantity\":7.0,\"price\":306.0,\"type\":\"SELL\"}]")));
    }

    private static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

}