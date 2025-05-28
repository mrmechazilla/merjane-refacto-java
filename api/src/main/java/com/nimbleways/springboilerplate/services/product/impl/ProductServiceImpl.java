package com.nimbleways.springboilerplate.services.product.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.product.NotificationService;
import com.nimbleways.springboilerplate.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;

    @Override
    public void notifyDelay(int leadTime, Product product) {
        product.setLeadTime(leadTime);
        productRepository.save(product);
        notificationService.sendDelayNotification(leadTime, product.getName());
    }

    @Override
    public void notifySeasonalUnavailable(Product product) {
        product.setAvailable(0);
        productRepository.save(product);
        notificationService.sendOutOfStockNotification(product.getName());
    }

    @Override
    public void notifyExpiration(Product product) {
        product.setAvailable(0);
        productRepository.save(product);
        notificationService.sendExpirationNotification(product.getName(), product.getExpiryDate());
    }
}