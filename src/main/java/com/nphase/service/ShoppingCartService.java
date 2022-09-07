package com.nphase.service;

import com.nphase.entity.Category;
import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShoppingCartService {

    private double discount = 0.9;
    private int quantityForDiscount = 3;


    public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts()
                .stream()
                .map(product -> product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal calculateTotalPriceWithDiscount(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts()
                .stream()
                .map(product -> {
                    BigDecimal result = product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()));
                    if (product.getQuantity() > quantityForDiscount) {
                        result = result.multiply(new BigDecimal(discount));
                    }
                    return result;
                })
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.DOWN);

    }

    public BigDecimal calculateTotalPriceWithDiscountByCategory(ShoppingCart shoppingCart) {
        //group by category
        Map<Category, List<Product>> categories = shoppingCart.getProducts()
                .stream()
                .collect(Collectors.groupingBy(Product::getCategory));

        BigDecimal result = BigDecimal.ZERO;

        for (Category category : categories.keySet()) {
            result = result.add(
                    categories.get(category)
                            .stream()
                            .map(product -> {
                                BigDecimal res = product.getPricePerUnit()
                                        .multiply(BigDecimal.valueOf(product.getQuantity()));
                                int count = categories.get(category)
                                        .stream()
                                        .map(Product::getQuantity)
                                        .mapToInt(Integer::intValue).sum();
                                if (count > quantityForDiscount) {
                                    res = res.multiply(new BigDecimal(discount));
                                }
                                return res;
                            })
                            .reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO));
        }

        return result.setScale(2, RoundingMode.DOWN);
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setQuantityForDiscount(int quantityForDiscount) {
        this.quantityForDiscount = quantityForDiscount;
    }
}
