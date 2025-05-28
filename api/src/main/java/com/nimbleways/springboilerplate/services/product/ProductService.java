package com.nimbleways.springboilerplate.services.product;

import com.nimbleways.springboilerplate.entities.Product;

public interface ProductService {
    void notifyDelay(int leadTime, Product product);

    void notifySeasonalUnavailable(Product product);

    void notifyExpiration(Product product);
}