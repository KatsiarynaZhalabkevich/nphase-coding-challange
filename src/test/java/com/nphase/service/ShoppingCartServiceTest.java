package com.nphase.service;


import com.nphase.entity.Category;
import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.util.Arrays;

public class ShoppingCartServiceTest {
    private final ShoppingCartService service = new ShoppingCartService();

    @Test
    public void calculatesPrice() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 2),
                new Product("Coffee", BigDecimal.valueOf(6.5), 1)
        ));

        BigDecimal result = service.calculateTotalPrice(cart);

        Assertions.assertEquals(result, BigDecimal.valueOf(16.5));
    }

    @Test
    public void calculatesPriceWithDiscount() {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 5),
                new Product("Coffee", BigDecimal.valueOf(3.5), 3)
        ));

        BigDecimal result = service.calculateTotalPriceWithDiscount(cart);

        Assertions.assertEquals(new BigDecimal("33.00"), result);
    }

    @Test
    public void calculateTotalPriceWithDiscountByCategory() {
        Product p1 = new Product("Tea", BigDecimal.valueOf(5.3), 2);
        p1.setCategory(Category.DRINKS);
        Product p2 = new Product("Coffee", BigDecimal.valueOf(3.5), 2);
        p2.setCategory(Category.DRINKS);
        Product p3 = new Product("Cheese", BigDecimal.valueOf(8), 2);
        p3.setCategory(Category.FOOD);

        ShoppingCart cart = new ShoppingCart(Arrays.asList(p1, p2, p3));

        BigDecimal result = service.calculateTotalPriceWithDiscountByCategory(cart);

        Assertions.assertEquals(new BigDecimal("31.84"), result);
    }

    @Test
    public void calculateTotalPriceWithDiscountByCategoryConfigurable() {
        service.setDiscount(0.9);
        service.setQuantityForDiscount(3);
        Product p1 = new Product("Tea", BigDecimal.valueOf(5.3), 2);
        p1.setCategory(Category.DRINKS);
        Product p2 = new Product("Coffee", BigDecimal.valueOf(3.5), 2);
        p2.setCategory(Category.DRINKS);
        Product p3 = new Product("Cheese", BigDecimal.valueOf(8), 2);
        p3.setCategory(Category.FOOD);

        ShoppingCart cart = new ShoppingCart(Arrays.asList(p1, p2, p3));

        BigDecimal result = service.calculateTotalPriceWithDiscountByCategory(cart);

        Assertions.assertEquals(new BigDecimal("31.84"), result);
    }



}