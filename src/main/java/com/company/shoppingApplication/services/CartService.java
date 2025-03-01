package com.company.shoppingApplication.services;

import com.company.shoppingApplication.entities.Cart;

import java.math.BigDecimal;

public interface CartService {

    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();

    Cart getCartByUserId(Long userId);
}
