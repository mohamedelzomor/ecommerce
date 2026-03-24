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

    // ===== صفحة تسجيل الدخول =====
    @GetMapping("/Auth/LoginPage")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "Auth/LoginPage";
    }

    // ===== صفحة التسجيل =====
    @GetMapping("/Auth/RegisterPage")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "Auth/RegisterPage";
    }

    // ===== معالجة تسجيل الدخول =====
    @PostMapping("/Auth/LoginPage")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userService.authenticate(email, password);

        if (user != null) {
            // ✅ تخزين المستخدم في الجلسة
            session.setAttribute("currentUser", user);

            // التحقق من صفحة تم طلبها قبل تسجيل الدخول
            String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
            if (redirectAfterLogin != null) {
                session.removeAttribute("redirectAfterLogin");
                return "redirect:" + redirectAfterLogin;
            }

            // توجيه المستخدم للصفحة الرئيسية بعد تسجيل الدخول
            return "redirect:/Home";
        }

        model.addAttribute("error", "البريد الإلكتروني أو كلمة المرور غير صحيحة!");
        return "Auth/LoginPage";
    }

    // ===== معالجة التسجيل =====
    @PostMapping("/Auth/RegisterPage")  // ← تعديل هنا
    public String register(@ModelAttribute User user, Model model) {

        // التحقق من أن البريد الإلكتروني غير موجود
        if (userService.getUserByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "البريد الإلكتروني موجود بالفعل!");
            return "Auth/RegisterPage";
        }

        // تشفير كلمة المرور وحفظ المستخدم
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);

        // إعادة التوجيه إلى صفحة تسجيل الدخول بعد التسجيل
        return "redirect:/Auth/LoginPage";
    }

    // ===== تسجيل الخروج =====
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/Auth/LoginPage";
    }
}