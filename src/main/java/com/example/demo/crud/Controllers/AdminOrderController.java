package com.example.demo.crud.Controllers;

import com.example.demo.crud.Models.Admin;
import com.example.demo.crud.Models.UserOrder;
import com.example.demo.crud.Services.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Admin") 
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/UserOrders")
    public String showAllConfirmedOrders(HttpSession session, Model model) {
        Admin currentAdmin = (Admin) session.getAttribute("currentAdmin");
        if (currentAdmin == null) {
            return "redirect:/AdminAuth/AdminLogin";
        }

        List<UserOrder> confirmedOrders = orderService.getConfirmedOrders();
        model.addAttribute("orders", confirmedOrders);
        return "UserOrders";
    }

    @PostMapping("/UserOrders/approve/{id}")
    public String approveOrder(HttpSession session, @PathVariable Long id) {
        Admin currentAdmin = (Admin) session.getAttribute("currentAdmin");
        if (currentAdmin == null) {
            return "redirect:/AdminAuth/AdminLogin";
        }

        orderService.approveOrder(id);
        return "redirect:/Admin/UserOrders";
    }


    @PostMapping("/UserOrders/delete/{id}")
    public String deleteOrder(HttpSession session, @PathVariable Long id) {
        Admin currentAdmin = (Admin) session.getAttribute("currentAdmin");
        if (currentAdmin == null) {
            return "redirect:/AdminAuth/AdminLogin";
        }

        orderService.deleteOrder(id);
        return "redirect:/Admin/UserOrders";
    }
    
}
