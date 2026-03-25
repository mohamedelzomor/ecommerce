package com.example.demo.crud.Controllers;

import com.example.demo.crud.Models.User;
import com.example.demo.crud.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/Auth/LoginPage")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "Auth/LoginPage";
    }

    @GetMapping("/Auth/RegisterPage")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "Auth/RegisterPage";
    }

    
    @PostMapping("/Auth/LoginPage")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userService.authenticate(email, password);

        if (user != null) {
    
            session.setAttribute("currentUser", user);

            String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
            if (redirectAfterLogin != null) {
                session.removeAttribute("redirectAfterLogin");
                return "redirect:" + redirectAfterLogin;
            }

            return "redirect:/Home";
        }

        model.addAttribute("error", "The email or password is incorrect!");
        return "Auth/LoginPage";
    }

    @PostMapping("/Auth/RegisterPage")  
        public String register(@ModelAttribute User user, Model model) {

        if (userService.getUserByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "The email already exists!");
            return "Auth/RegisterPage";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);

        return "redirect:/Auth/LoginPage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/Auth/LoginPage";
    }
}