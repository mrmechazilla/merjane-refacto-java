package com.nimbleways.springboilerplate.services.product.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.product.BaseProductProcessor;
import com.nimbleways.springboilerplate.services.product.ProductProcessor;
import com.nimbleways.springboilerplate.services.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NormalProductProcessor extends BaseProductProcessor implements ProductProcessor {
    private final ProductService productService;

    public NormalProductProcessor(ProductRepository productRepository, ProductService productService) {
        super(productRepository);
        this.productService = productService;
    }
    @Override
    public void process(Product product) {
        if (product.isAvailable()) {
            decrementStock(product);
        } else if (product.getLeadTime() != null && product.getLeadTime() > 0) {
            productService.notifyDelay(product.getLeadTime(), product);
        } else {
            log.warn("No stock and no lead time for product: {}", product.getName());
        }
    }
}
