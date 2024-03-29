package com.example.orderservice.service.web;

import com.example.orderservice.controller.OrderController;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.example.orderservice.Utils.asJsonString;
import static com.example.orderservice.service.TestEntityProvider.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
@TestPropertySources({
        @TestPropertySource(properties = "spring.cloud.config.enabled=false"),
        @TestPropertySource(properties = "spring.cloud.discovery.enabled=false")
})
public class OrderRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderMapper orderMapper;

    @Test
    public void createOrderAPI() throws Exception {
        when(orderService.createOrder(any())).thenReturn(buildOrder());

        mvc.perform(MockMvcRequestBuilders
                        .post("/orders/create")
                        .content(asJsonString(buildOrderRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderComment").exists());
    }

    @Test
    public void updateOrderAPI() throws Exception {
        Order order = buildOrder();
        when(orderService.updateOrder(any(), any())).thenReturn(order);

        mvc.perform(MockMvcRequestBuilders
                        .put("/orders/update/{orderId}",
                                order.getPatientId(), order.getOrderId())
                        .content(asJsonString(order))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.orderComment").value(order.getOrderComment()));
    }

    @Test
    public void declineOrderAPI() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(
                        "/orders/decline/{orderId}", faker.idNumber().invalid()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getOrdersByPatientIdsAndPatientStateAPI() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/orders?patientIds=1,2,3&patientState=ACTIVE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
