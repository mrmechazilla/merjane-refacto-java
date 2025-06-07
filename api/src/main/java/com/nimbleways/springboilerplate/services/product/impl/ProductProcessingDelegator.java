package com.nimbleways.springboilerplate.services.product.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.services.product.ProductProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductProcessingDelegator {

    private final Map<ProductType, ProductProcessor> processors;

    public void process(Product product) {
        ProductProcessor processor = processors.get(product.getType());
        if (processor == null) {
            throw new IllegalStateException("No processor found for product category: " + product.getType());
        }
        processor.process(product);
    }
}
