package com.nimbleways.springboilerplate.config;

import com.nimbleways.springboilerplate.entities.enums.ProductType;
import com.nimbleways.springboilerplate.services.product.ProductProcessor;
import com.nimbleways.springboilerplate.services.product.impl.ExpirableProductProcessor;
import com.nimbleways.springboilerplate.services.product.impl.NormalProductProcessor;
import com.nimbleways.springboilerplate.services.product.impl.SeasonalProductProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ProcessorConfig {
    @Bean
    public Map<ProductType, ProductProcessor> processorMap(
            NormalProductProcessor normal,
            ExpirableProductProcessor expirable,
            SeasonalProductProcessor seasonal
    ) {
        return Map.of(
                ProductType.NORMAL, normal,
                ProductType.EXPIRABLE, expirable,
                ProductType.SEASONAL, seasonal
        );
    }

}
