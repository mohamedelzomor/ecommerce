package com.example.demo.crud.Services;

import com.example.demo.crud.Models.CartItem;
import com.example.demo.crud.Models.UserOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final OrderService orderService;

    public UserOrder getCurrentCart(Long userId) {
        return orderService.getCurrentCart(userId);
    }

    public List<CartItem> getCartItems(Long userId) {
        UserOrder cart = orderService.getCurrentCart(userId);
        if (cart != null && cart.getCartItems() != null) {
            return cart.getCartItems();
        }
        return List.of();
    }

    public void updateQuantity(Long userId, Long itemId, int quantity) {
        orderService.updateCartItemQuantity(userId, itemId, quantity);
    }

    public void removeFromCart(Long userId, Long itemId) {
        orderService.removeCartItem(userId, itemId);
    }

    public void addToCart(Long userId, Long productId, int quantity) {
        orderService.addToCart(userId, productId, quantity);
    }
}