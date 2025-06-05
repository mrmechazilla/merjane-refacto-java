package com.nimbleways.springboilerplate.utils;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.entities.enums.ProductType;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class InventoryTestDataFactory {

    public static Product createProduct(ProductType category, int leadTime, int available, String name) {
        return new Product(null, leadTime, available, category, name, null, null, null);
    }

    public static Product createExpirableProduct(String name, int leadTime, int available, LocalDate expirationDate) {
        return new Product(null, leadTime, available, ProductType.EXPIRABLE, name, expirationDate, null, null);
    }

    public static Order createOrder(Set<Product> products) {
        Order order = new Order();
        order.setItems(products);
        return order;
    }

    public static List<Product> createTestProducts() {
        LocalDate now = LocalDate.now();
        Product watermelon = createProduct(ProductType.SEASONAL, 15, 30, "Pumpkin");
        watermelon.setSeasonStartDate(now.minusDays(2));
        watermelon.setSeasonEndDate(now.plusDays(58));

        Product grapes = createProduct(ProductType.SEASONAL, 15, 30, "Strawberries");
        grapes.setSeasonStartDate(now.plusDays(180));
        grapes.setSeasonEndDate(now.plusDays(240));

        return List.of(
                createProduct(ProductType.NORMAL, 15, 30, "Headphones"),
                createProduct(ProductType.NORMAL, 10, 0, "Mouse Pad"),
                createExpirableProduct("Yogurt", 15, 30, now.plusDays(26)),
                createExpirableProduct("Eggs", 90, 6, now.minusDays(2)),
                watermelon,
                grapes
        );
    }
}

