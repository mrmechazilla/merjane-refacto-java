package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.product.NotificationService;
import com.nimbleways.springboilerplate.utils.InventoryTestDataFactory;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;

// import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Specify the controller class you want to test
// This indicates to spring boot to only load UsersController into the context
// Which allows a better performance and needs to do less mocks
@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @MockBean
    private NotificationService notificationService;

    @ParameterizedTest
    @ValueSource(strings = {"/v1/orders/{orderId}/process", "/orders/{orderId}/processOrder"})
    public void processOrderShouldSucceedForAllRoutes(String urlTemplate) throws Exception {
        // GIVEN
        List<Product> allProducts = InventoryTestDataFactory.createTestProducts();
        Set<Product> orderItems = new HashSet<>(allProducts);
        Order order = InventoryTestDataFactory.createOrder(orderItems);

        productRepository.saveAll(allProducts);
        order = orderRepository.save(order);

        // WHEN
        mockMvc.perform(post(urlTemplate, order.getId())
                            .contentType("application/json"))
                    .andExpect(status().isOk());

        // THEN
        Order resultOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertEquals(order.getId(), resultOrder.getId());
    }
}
