package com.example.demo.crud.Controllers;

import com.example.demo.crud.Models.*;
import com.example.demo.crud.Repositories.*;
// import com.example.demo.crud.Services.OrderService;
// import com.example.demo.crud.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserUIController {

    // @Autowired
    // private OrderService orderService;

    // @Autowired
    // private UserService userService;

    // @Autowired
    // private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping("/Home")
    public String home(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        // if (currentUser == null) {
        //     session.setAttribute("redirectAfterLogin", "/Home");
        //     return "redirect:/Auth/LoginPage";
        // }

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("title", "Home");
        model.addAttribute("currentUser", currentUser);

        return "Home";
    }

    @GetMapping("/Category/{id}")
public String viewCategory(@PathVariable Long id, Model model, HttpSession session) {

    Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));

    List<Product> products = productRepository.findByCategoryId(id);

    User currentUser = (User) session.getAttribute("currentUser"); 

    model.addAttribute("category", category);
    model.addAttribute("products", products);
    model.addAttribute("currentUser", currentUser);

    return "Categories/CategoryProducts";
    }

    @GetMapping("/Categories")
    public String listCategories(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            session.setAttribute("redirectAfterLogin", "/Categories");
            return "redirect:/Auth/LoginPage";
        }

        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("currentUser", currentUser);

        return "CategoryList";
    }
}
