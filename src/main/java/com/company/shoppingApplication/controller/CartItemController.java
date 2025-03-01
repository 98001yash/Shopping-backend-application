package com.company.shoppingApplication.controller;

import com.company.shoppingApplication.Exceptions.ResourceNotFoundException;
import com.company.shoppingApplication.config.ApiResponse;
import com.company.shoppingApplication.services.CartItemService;
import com.company.shoppingApplication.services.CartService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartService cartService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId,
                                                     @RequestParam(required = false) Long productId,
                                                     @RequestParam(required = false) Integer quantity) {
        try {
            if (cartId == null) {
                cartId = cartService.initializeNewCart();
            }
            if (productId == null || quantity == null) {
                return ResponseEntity.badRequest().body(new ApiResponse("Product ID and Quantity are required", null));
            }
            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Add-item-Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {

        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Remove-item-Success", null));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long itemId,
                                                          @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Update-item-quantity-success", null));
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
