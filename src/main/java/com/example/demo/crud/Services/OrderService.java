package com.example.demo.crud.Services;

import com.example.demo.crud.Models.CartItem;
import com.example.demo.crud.Models.Product;
import com.example.demo.crud.Models.User;
import com.example.demo.crud.Models.UserOrder;
import com.example.demo.crud.Repositories.ProductRepository;
import com.example.demo.crud.Repositories.UserOrderRepository;
import com.example.demo.crud.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserOrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public void addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ User not found"));

        UserOrder order = orderRepository.findByUserIdAndStatus(userId, "IN_CART")
                .orElseGet(() -> orderRepository.save(new UserOrder(user, "IN_CART")));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("❌ Product not found"));

        Optional<CartItem> existingItem = order.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            order.addCartItem(new CartItem(order, product, quantity));
        }

        order.calculateTotalPrice();
        orderRepository.save(order);
    }

    public UserOrder getCurrentCart(Long userId) {
        return orderRepository.findByUserIdAndStatus(userId, "IN_CART").orElse(null);
    }

    public void updateCartItemQuantity(Long userId, Long itemId, int quantity) {
        UserOrder order = getCurrentCart(userId);
        if (order == null) return;

        order.getCartItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));

        order.calculateTotalPrice();
        orderRepository.save(order);
    }

    public void removeCartItem(Long userId, Long itemId) {
        UserOrder order = getCurrentCart(userId);
        if (order == null) return;

        boolean removed = order.getCartItems().removeIf(item -> item.getId().equals(itemId));
        if (removed) {
            order.calculateTotalPrice();
            orderRepository.save(order);
        }
    }

    public void saveOrder(UserOrder order) {
        if (order != null) {
            order.calculateTotalPrice();
            orderRepository.save(order);
        }
    }

    public List<UserOrder> getConfirmedOrders() {
        return orderRepository.findByStatus("CONFIRMED");
    }

    public void approveOrder(Long id) {
        UserOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Order not found with id: " + id));

        if (order.getCartItems() == null || order.getCartItems().isEmpty()) {
            throw new RuntimeException("⚠️ Cart is empty. Cannot approve this order.");
        }

        order.calculateTotalPrice();

        for (CartItem item : order.getCartItems()) {
            Product product = item.getProduct();
            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("⚠️ Not enough stock for product: " + product.getTitle());
            }
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus("CONFIRMED");
        orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("❌ Cannot delete: Order not found");
        }
        orderRepository.deleteById(id);
    }
}