package com.example.demo.crud.Controllers;

import com.example.demo.crud.Models.User;
import com.example.demo.crud.Models.UserOrder;
import com.example.demo.crud.Services.CartService;
import com.example.demo.crud.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    private User getCurrentUser(HttpSession session, String redirectPath) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            session.setAttribute("redirectAfterLogin", redirectPath);
        }
        return user;
    }

    @GetMapping("/CartPage")
    public String showCartPage(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session, "/CartPage");
        if (currentUser == null) return "redirect:/Auth/LoginPage";

        UserOrder cart = cartService.getCurrentCart(currentUser.getId());
        model.addAttribute("cartItems", cart != null ? cart.getCartItems() : null);
        model.addAttribute("total", cart != null ? cart.getTotalPrice() : 0);
        model.addAttribute("currentUser", currentUser);

        return "CartPage";
    }

    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<String> addToCartAjax(@RequestParam Long productId,
                                                @RequestParam int quantity,
                                                HttpSession session) throws Exception {

        User currentUser = getCurrentUser(session, "/CartPage");
        if (currentUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        cartService.addToCart(currentUser.getId(), productId, quantity);
        return ResponseEntity.ok("OK"); 
    }

    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam("itemId") Long itemId,
                                 @RequestParam("quantity") int quantity,
                                 HttpSession session) throws Exception {

        User currentUser = getCurrentUser(session, "/CartPage");
        if (currentUser == null) return "redirect:/Auth/LoginPage";

        cartService.updateQuantity(currentUser.getId(), itemId, quantity);
        return "redirect:/CartPage";
    }


    @PostMapping("/cart/delete")
    public String deleteItem(@RequestParam("itemId") Long itemId,
                             HttpSession session) throws Exception {

        User currentUser = getCurrentUser(session, "/CartPage");
        if (currentUser == null) return "redirect:/Auth/LoginPage";

        cartService.removeFromCart(currentUser.getId(), itemId);
        return "redirect:/CartPage";
    }
}
