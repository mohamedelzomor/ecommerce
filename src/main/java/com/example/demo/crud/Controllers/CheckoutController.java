package com.example.demo.crud.Controllers;

import com.example.demo.crud.Models.User;
import com.example.demo.crud.Models.UserOrder;
import com.example.demo.crud.Services.OrderService;
import com.example.demo.crud.config.UploadProperties;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private OrderService orderService;

    // قيمة افتراضية لمجلد رفع الملفات إذا الخاصية غير موجودة في application.properties
    private final UploadProperties uploadProperties;

@Autowired
public CheckoutController(UploadProperties uploadProperties) {
    this.uploadProperties = uploadProperties;
}
    private final List<String> allowedFileTypes = Arrays.asList(
            "image/png", "image/jpeg", "image/jpg", "image/gif"
    );


    // ===== عرض صفحة الدفع =====
    @GetMapping("/DepositPage")
    public String showDepositPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        // إذا المستخدم غير مسجل الدخول → تخزين الصفحة المطلوبة وإعادة التوجيه
        if (currentUser == null) {
            session.setAttribute("redirectAfterLogin", "/DepositPage");
            return "redirect:/Auth/LoginPage";
        }

        UserOrder cart = orderService.getCurrentCart(currentUser.getId());
        model.addAttribute("cartItems", cart != null ? cart.getCartItems() : null);
        model.addAttribute("total", cart != null ? cart.getTotalPrice() : 0);

        return "DepositPage";
    }

    // @GetMapping("/confirmPayment")
    // public String confirmPaymentPage() {
    // return "redirect:/Home"; // أو أي صفحة مناسبة
    // }
    @GetMapping("/Confirm")
    public String confirmPaymentPage() {
    return "Confirm"; // اسم صفحة HTML
    }
    
    // ===== تأكيد الدفع =====
    @PostMapping("/confirmPayment")
public String confirmPayment(HttpSession session,
                             @RequestParam("paymentImage") MultipartFile paymentImage,
                             Model model) {

    User currentUser = (User) session.getAttribute("currentUser");
    if (currentUser == null) {
        session.setAttribute("redirectAfterLogin", "/DepositPage");
        return "redirect:/Auth/LoginPage";
    }

    UserOrder cart = orderService.getCurrentCart(currentUser.getId());
    if (cart == null) return "redirect:/DepositPage";

    try {
        if (paymentImage != null && !paymentImage.isEmpty()) {
            // التحقق من نوع الملف
            List<String> allowedFileTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg", "image/gif");
            if (!allowedFileTypes.contains(paymentImage.getContentType())) {
                model.addAttribute("error", "نوع الملف غير مدعوم!");
                return "DepositPage";
            }

            // اسم فريد للملف
            String fileName = System.currentTimeMillis() + "_" + paymentImage.getOriginalFilename();

            // مجلد خارجي دائم
            File dir = new File(uploadProperties.getPaymentDir());
            if (!dir.exists()) dir.mkdirs(); // إنشاء المجلد لو مش موجود

            // حفظ الملف
            File dest = new File(dir, fileName);
            paymentImage.transferTo(dest);

            // حفظ اسم الملف في الطلب
            cart.setPaymentImage(fileName);
        }

        cart.setStatus("CONFIRMED");
        orderService.saveOrder(cart);

    } catch (IOException e) {
        e.printStackTrace();
        model.addAttribute("error", "حدث خطأ أثناء رفع الملف!");
        return "DepositPage";
    }

    return "redirect:/Confirm";
}

    // @GetMapping("/confirmPayment")
    // public String confirmPaymentPage() {
    // return "Confirm"; // أو أي صفحة مناسبة
    // }

}
