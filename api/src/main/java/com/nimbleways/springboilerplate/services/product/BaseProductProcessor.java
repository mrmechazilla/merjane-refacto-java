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
        product.setAvailable(product.getAvailable() - 1);
        productRepository.save(product);
    }

    protected void markUnavailable(Product product) {
        product.setAvailable(0);
        productRepository.save(product);
    }

    protected boolean isAvailable(Product product) {
        return product.getAvailable() != null && product.getAvailable() > 0;
    }

    protected boolean isExpired(Product product) {
        return product.getExpiryDate() != null && product.getExpiryDate().isBefore(LocalDate.now());
    }

    protected boolean isInSeason(Product product, LocalDate date) {
        return product.getSeasonStartDate() != null &&
                product.getSeasonEndDate() != null &&
                !date.isBefore(product.getSeasonStartDate()) &&
                !date.isAfter(product.getSeasonEndDate());
    }
}
