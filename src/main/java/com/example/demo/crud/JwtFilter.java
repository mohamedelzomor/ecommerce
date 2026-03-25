package com.example.demo.crud;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }


        Object currentUser = request.getSession().getAttribute("currentUser");

        if (currentUser == null) {

            request.getSession().setAttribute("redirectAfterLogin", path);


            if (!response.isCommitted()) {
                response.sendRedirect(request.getContextPath() + "/Auth/LoginPage");
            }
            return;
        }


        filterChain.doFilter(request, response);
    }



    private boolean isPublicPath(String path) {
        return path.equals("/") 
                || path.equalsIgnoreCase("/home")
                || path.equals("/favicon.ico")   
                || path.startsWith("/Auth/")
                || path.startsWith("/AdminAuth/")
                || path.toLowerCase().startsWith("/category")
                || path.startsWith("/uploads/")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/webjars/")
                || path.matches(".+\\.(css|js|png|jpg|jpeg|gif|svg|ico)$")
                || path.startsWith("/cart/")
                || path.startsWith("/CartPage")
                || path.startsWith("/DepositPage")
                ;
    }
}