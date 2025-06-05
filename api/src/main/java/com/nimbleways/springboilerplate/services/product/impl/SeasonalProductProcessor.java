package com.nimbleways.springboilerplate.services.product.impl;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.product.BaseProductProcessor;
import com.nimbleways.springboilerplate.services.product.NotificationService;
import com.nimbleways.springboilerplate.services.product.ProductProcessor;
import com.nimbleways.springboilerplate.services.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class SeasonalProductProcessor extends BaseProductProcessor implements ProductProcessor {

    private final NotificationService notificationService;
    private final ProductService productService;

    public SeasonalProductProcessor(ProductRepository productRepository,
                                    NotificationService notificationService,
                                    ProductService productService) {
        super(productRepository);
        this.notificationService = notificationService;
        this.productService = productService;
    }

    @Override
    public boolean supports(Product product) {
        return ProductType.SEASONAL.equals(product.getType());
    }

    @Override
    public void process(Product product) {
        LocalDate now = LocalDate.now();

        boolean inSeason = product.isInSeason(now);
        boolean restockAfterSeason = product.getLeadTime() != null &&
                now.plusDays(product.getLeadTime()).isAfter(product.getSeasonEndDate());

        if (inSeason && product.isAvailable()) {
            decrementStock(product);
        } else if (restockAfterSeason || product.getSeasonStartDate().isAfter(now)) {
            markUnavailable(product);
            notificationService.sendOutOfStockNotification(product.getName());
        } else {
            productService.notifyDelay(product.getLeadTime(), product);
        }
    }
}
