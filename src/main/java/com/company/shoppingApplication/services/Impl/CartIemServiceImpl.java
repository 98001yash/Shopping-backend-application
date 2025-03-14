package com.company.shoppingApplication.services.Impl;


import com.company.shoppingApplication.Exceptions.ResourceNotFoundException;
import com.company.shoppingApplication.entities.Cart;
import com.company.shoppingApplication.entities.CartItem;
import com.company.shoppingApplication.entities.Product;
import com.company.shoppingApplication.repository.CartItemRepository;
import com.company.shoppingApplication.repository.CartRepository;
import com.company.shoppingApplication.services.CartItemService;
import com.company.shoppingApplication.services.CartService;
import com.company.shoppingApplication.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class CartIemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final CartRepository cartRepository;



    @Override
    public void addItemToCart(Long cartId,Long productId, int quantity) {
      // get the cart
        // get the Product
        // check if the product is already exist in the cart
        // if yes, then increase the quantity with the requested quantity
        // if no, then initiate the cartIem entry

        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
       CartItem cartItem  = cart.getItems()
               .stream()
               .filter(item-> item.getProduct().getId().equals(productId))
               .findFirst().orElse(new CartItem());
       if(cartItem.getId()==null){
           cartItem.setCart(cart);
           cartItem.setProduct(product);
           cartItem.setQuantity(quantity);
           cartItem.setUnitPrice(product.getPrice());
       }
       else {
           cartItem.setQuantity(cartItem.getQuantity()+ quantity);
       }
       cartItem.setTotalPrice();
       cart.addItem(cartItem);
       cartItemRepository.save(cartItem);
       cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
      Cart cart = cartService.getCart(cartId);
      CartItem itemToRemove = getCartItem(cartId, productId);
         cart.removeItem(itemToRemove);
        cartRepository.save(cart);

    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
      Cart cart = cartService.getCart(cartId);
      cart.getItems()
              .stream()
              .filter(item-> item.getProduct().getId().equals(productId))
              .findFirst()
              .ifPresent(item -> {
                  item.setQuantity(quantity);
                  item.setUnitPrice(item.getProduct().getPrice());
                  item.setTotalPrice();
              });
      BigDecimal totalAmount = cart.getItems()
              .stream()
              .map(CartItem::getTotalPrice)
              .reduce(BigDecimal.ZERO, BigDecimal::add);

      cart.setTotalAmount(totalAmount);
      cartRepository.save(cart);
    }


    @Override
    public CartItem getCartItem(Long cartId, Long productId){
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(()->new ResourceNotFoundException("Product not found"));
    }
}
