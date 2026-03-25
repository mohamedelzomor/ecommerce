package com.example.demo.crud.Controllers;

import com.example.demo.crud.Models.User;
import com.example.demo.crud.Models.UserOrder;
import com.example.demo.crud.Services.OrderService;
import com.example.demo.crud.config.UploadProperties;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
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

    private final UploadProperties uploadProperties;

@Autowired
public CheckoutController(UploadProperties uploadProperties) {
    this.uploadProperties = uploadProperties;
}
    private final List<String> allowedFileTypes = Arrays.asList(
            "image/png", "image/jpeg", "image/jpg", "image/gif"
    );


    @GetMapping("/DepositPage")
    public String showDepositPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");


        if (currentUser == null) {
            session.setAttribute("redirectAfterLogin", "/DepositPage");
            return "redirect:/Auth/LoginPage";
        }

        UserOrder cart = orderService.getCurrentCart(currentUser.getId());
        model.addAttribute("cartItems", cart != null ? cart.getCartItems() : null);
        model.addAttribute("total", cart != null ? cart.getTotalPrice() : 0);

        return "DepositPage";
    }

    @GetMapping("/Confirm")
    public String confirmPaymentPage() {
    return "Confirm"; 
    }
    
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

            List<String> allowedFileTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg", "image/gif");
            if (!allowedFileTypes.contains(paymentImage.getContentType())) {
                model.addAttribute("error","File type not supported!");
                return "DepositPage";
            }


            String fileName = System.currentTimeMillis() + "_" + paymentImage.getOriginalFilename();


            File dir = new File(uploadProperties.getPaymentDir());
            if (!dir.exists()) dir.mkdirs(); 

            File dest = new File(dir, fileName);
            paymentImage.transferTo(dest);


            cart.setPaymentImage(fileName);
        }

        cart.setStatus("CONFIRMED");
        orderService.saveOrder(cart);

    } catch (IOException e) {
        e.printStackTrace();
        model.addAttribute("error", "An error occurred while uploading the file!");
        return "DepositPage";
    }

    return "redirect:/Confirm";
}


}
