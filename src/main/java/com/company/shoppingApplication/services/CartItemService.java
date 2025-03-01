package com.company.shoppingApplication.services;


import com.company.shoppingApplication.entities.CartItem;

public interface CartItemService {

    void addItemToCart(Long cartId, Long product, int quantity);

    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);


    CartItem getCartItem(Long cartId, Long productId);
}
