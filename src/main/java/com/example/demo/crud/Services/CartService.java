package com.example.demo.crud.Services;

import com.example.demo.crud.Models.CartItem;
import com.example.demo.crud.Models.UserOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private OrderService orderService;

    // ✅ استرجاع الكارت الحالي للمستخدم
    public UserOrder getCurrentCart(Long userId) {
        return orderService.getCurrentCart(userId);
    }

    // ✅ الحصول على جميع عناصر السلة للمستخدم الحالي
    public List<CartItem> getCartItems(Long userId) {
        UserOrder cart = orderService.getCurrentCart(userId);
        if (cart != null && cart.getCartItems() != null) {
            return cart.getCartItems();
        }
        return List.of(); // ترجع قائمة فارغة لو مفيش كارت أو مفيش عناصر
    }

    // ✅ تحديث كمية عنصر في السلة
    public void updateQuantity(Long userId, Long itemId, int quantity) {
        orderService.updateCartItemQuantity(userId, itemId, quantity);
    }

    // ✅ حذف عنصر من السلة
    public void removeFromCart(Long userId, Long itemId) {
        orderService.removeCartItem(userId, itemId);
    }

    // ✅ إضافة منتج جديد للسلة
    public void addToCart(Long userId, Long productId, int quantity) {
        orderService.addToCart(userId, productId, quantity);
    }
}
