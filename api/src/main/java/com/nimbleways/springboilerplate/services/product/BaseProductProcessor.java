package com.nimbleways.springboilerplate.services.product;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseProductProcessor implements ProductProcessor{
    protected final ProductRepository productRepository;

    protected void decrementStock(Product product) {
        product.decrementStock();
        productRepository.save(product);
    }

    protected void markUnavailable(Product product) {
        product.markUnavailable();
        productRepository.save(product);
    }

}
