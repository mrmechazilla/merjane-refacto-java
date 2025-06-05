package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.product.NotificationService;
import com.nimbleways.springboilerplate.services.product.impl.ExpirableProductProcessor;
import com.nimbleways.springboilerplate.services.product.impl.NormalProductProcessor;
import com.nimbleways.springboilerplate.services.product.impl.ProductServiceImpl;
import com.nimbleways.springboilerplate.utils.Annotations.UnitTest;

import com.nimbleways.springboilerplate.utils.InventoryTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@UnitTest
public class ProductProcessingUnitTest {

    @Mock
    private NotificationService notificationService;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks 
    private ProductServiceImpl productService;

    private NormalProductProcessor normalProductProcessor;
    private ExpirableProductProcessor expirableProductProcessor;
    @BeforeEach
    void initializeProcessors() {
         normalProductProcessor = new NormalProductProcessor(productRepository, productService);
         expirableProductProcessor = new ExpirableProductProcessor(productRepository, notificationService);
    }

    @Test
    void shouldNotifyDelayAndSaveProduct() {
        // GIVEN
        Product product = InventoryTestDataFactory.createProduct(ProductType.NORMAL, 15, 10, "Monitor");
        when(productRepository.save(product)).thenReturn(product);

        // WHEN
        productService.notifyDelay(product.getLeadTime(), product);

        // THEN
        assertEquals(15, product.getLeadTime());
        verify(productRepository).save(product);
        verify(notificationService).sendDelayNotification(15, "Monitor");
    }

    @Test
    void shouldDecrementStockForNormalProductIfAvailable() {
        // GIVEN
        Product product = InventoryTestDataFactory.createProduct(ProductType.NORMAL, 5, 10, "Speaker");

        // WHEN
        normalProductProcessor.process(product);

        // THEN
        assertEquals(9, product.getAvailable());
        verify(productRepository).save(product);
    }

    @Test
    void shouldNotifyIfProductIsExpired() {
        // GIVEN
        LocalDate expiredDate = LocalDate.now().minusDays(1);
        Product expiredProduct = InventoryTestDataFactory.createExpirableProduct("Milk", 5, 1, expiredDate);

        // WHEN
        expirableProductProcessor.process(expiredProduct);

        // THEN
        assertEquals(0, expiredProduct.getAvailable());
        verify(notificationService).sendExpirationNotification("Milk", expiredDate);
        verify(productRepository).save(expiredProduct);
    }

    @Test
    void shouldDecrementStockIfExpirableProductNotExpired() {
        // GIVEN
        LocalDate expiration = LocalDate.now().plusDays(1);
        Product freshProduct = InventoryTestDataFactory.createExpirableProduct("Juice", 5, 3, expiration);

        // WHEN
        expirableProductProcessor.process(freshProduct);

        // THEN
        assertEquals(2, freshProduct.getAvailable());
        verify(productRepository).save(freshProduct);
        verifyNoInteractions(notificationService);
    }
}