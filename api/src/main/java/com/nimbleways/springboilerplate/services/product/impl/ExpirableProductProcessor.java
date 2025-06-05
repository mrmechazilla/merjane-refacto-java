package com.nimbleways.springboilerplate.services.product.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.product.BaseProductProcessor;
import com.nimbleways.springboilerplate.services.product.NotificationService;
import com.nimbleways.springboilerplate.services.product.ProductProcessor;
import org.springframework.stereotype.Service;

@Service
public class ExpirableProductProcessor extends BaseProductProcessor implements ProductProcessor {

    private final NotificationService notificationService;

    public ExpirableProductProcessor(ProductRepository productRepository, NotificationService notificationService) {
        super(productRepository);
        this.notificationService = notificationService;
    }

    @Override
    public boolean supports(Product product) {
        return ProductType.EXPIRABLE.equals(product.getType());
    }

    @Override
    public void process(Product product) {
        if (product.isAvailable() && !product.isExpired()) {
            decrementStock(product);
        } else {
            notificationService.sendExpirationNotification(product.getName(), product.getExpiryDate());
            markUnavailable(product);
        }
    }
}
