// package com.example.demo.crud;

// import io.jsonwebtoken.*;
// import io.jsonwebtoken.security.Keys;
// import jakarta.servlet.*;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.stereotype.Component;

// import java.io.IOException;
// import java.nio.charset.StandardCharsets;
// import java.security.Key;
// import java.util.Date;

// @Component
// public class JwtHandler implements Filter {

//     // 🔑 مفتاح سري ثابت وطويل بما فيه الكفاية (على الأقل 32 بايت)
//     private static final String SECRET_KEY = "01234567890123456789012345678901"; // 32 حرف

//     // ===================== JWT Utility Methods =====================
//     private static Key getSigningKey() {
//         return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//     }

//     public static String generateToken(Long userId, String email) {
//         Key key = getSigningKey();

//         return Jwts.builder()
//                 .claim("userId", userId)
//                 .claim("email", email)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 5))) // 5 ساعات
//                 .signWith(key, SignatureAlgorithm.HS256)
//                 .compact();
//     }

//     public static Claims validateToken(String token) throws Exception {
//         try {
//             return Jwts.parserBuilder()
//                     .setSigningKey(getSigningKey())
//                     .build()
//                     .parseClaimsJws(token)
//                     .getBody();
//         } catch (JwtException e) {
//             throw new Exception("Invalid JWT token: " + e.getMessage());
//         }
//     }

//     // ===================== JWT Filter =====================
//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//             throws IOException, ServletException {

//         HttpServletRequest req = (HttpServletRequest) request;
//         HttpServletResponse res = (HttpServletResponse) response;

//         String path = req.getRequestURI();

//         // 🟢 المسارات المفتوحة بدون تحقق JWT
//         if (
//                 path.startsWith("/api/auth") ||
//                 path.startsWith("/User") ||
//                 path.startsWith("/Home") ||
//                 path.startsWith("/uploads") ||
//                 path.startsWith("/css") ||
//                 path.startsWith("/js") ||
//                 path.startsWith("/images") ||
//                 path.equals("/")
//         ) {
//             chain.doFilter(request, response);
//             return;
//         }

//         // 🟠 التحقق من وجود التوكن
//         String authHeader = req.getHeader("Authorization");

//         if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//             res.sendRedirect("/Auth/LoginPage");
//             return;
//         }

//         String token = authHeader.substring(7);

//         try {
//             Claims claims = validateToken(token);
//             request.setAttribute("userId", claims.get("userId"));
//             chain.doFilter(request, response);
//         } catch (Exception e) {
//             res.sendRedirect("/Auth/LoginPage");
//         }
//     }
// }
