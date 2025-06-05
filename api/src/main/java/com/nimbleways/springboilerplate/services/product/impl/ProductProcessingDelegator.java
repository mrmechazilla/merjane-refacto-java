package com.nimbleways.springboilerplate.services.product.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.services.product.ProductProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductProcessingDelegator {

    private final List<ProductProcessor> processors;

    public void process(Product product) {
        processors.stream()
                .filter(p -> p.supports(product))
                .findFirst()
                .ifPresentOrElse(
                        processor -> {
                            processor.process(product);
                        },
                        () -> {
                            throw new IllegalStateException("No processor found for product type: " + product.getType());
                        }
                );
    }
}
