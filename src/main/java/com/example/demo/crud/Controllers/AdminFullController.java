package com.example.demo.crud.Controllers;

import com.example.demo.crud.Models.*;
import com.example.demo.crud.Product.ProductDTO;
import com.example.demo.crud.Repositories.*;
import com.example.demo.crud.Services.OrderService;
import com.example.demo.crud.config.UploadProperties;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;

@Controller
public class AdminFullController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final UploadProperties uploadProperties;

    @Autowired
    public AdminFullController(ProductRepository productRepository,
                               CategoryRepository categoryRepository,
                               AdminRepository adminRepository,
                               UserRepository userRepository,
                               OrderService orderService,
                               UploadProperties uploadProperties) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.uploadProperties = uploadProperties;
    }

    // // =================== الصفحة الرئيسية ===================
    // @GetMapping({ "/Home"})
    // public String home(Model model) {
    //     model.addAttribute("categories", categoryRepository.findAll());
    //     return "Home";
    // }

    // =================== إدارة Categories ===================
    @GetMapping("/AdminCategories/Categories")
    public String categoriesPage(Model model,
                                 @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";
        model.addAttribute("categories", categoryRepository.findAll());
        return "AdminCategories/Categories";
    }

    @GetMapping("/AdminCategories/AddCategory")
    public String showAddCategoryForm(Model model,
                                      @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";
        model.addAttribute("category", new Category());
        return "AdminCategories/AddCategory";
    }

    @PostMapping("/AdminCategories/AddCategory")
    public String addCategory(@RequestParam("title") String title,
                              @RequestParam("image") MultipartFile file,
                              @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) throws IOException {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        Category category = new Category();
        category.setTitle(title);

        if (!file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            category.setImage(fileName);

            Path uploadPath = Paths.get(uploadProperties.getCategoriesDir());
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        categoryRepository.save(category);
        return "redirect:/AdminCategories/Categories";
    }

    @GetMapping("/AdminCategories/EditCategory")
    public String showEditCategoryForm(@RequestParam Long id, Model model,
                                       @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + id));
        model.addAttribute("category", category);
        return "AdminCategories/EditCategory";
    }

    @PostMapping("/AdminCategories/update")
    public String updateCategory(@RequestParam Long id,
                                 @RequestParam("title") String title,
                                 @RequestParam("image") MultipartFile file,
                                 @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) throws IOException {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + id));
        category.setTitle(title);

        if (!file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            category.setImage(fileName);

            Path uploadPath = Paths.get(uploadProperties.getCategoriesDir());
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        categoryRepository.save(category);
        return "redirect:/AdminCategories/Categories";
    }

    @GetMapping("/AdminCategories/delete/{id}")
    public String deleteCategory(@PathVariable Long id,
                                 @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";
        categoryRepository.deleteById(id);
        return "redirect:/AdminCategories/Categories";
    }

    // =================== إدارة المنتجات ===================
    @GetMapping("/AdminProducts")
    public String getAllProducts(Model model,
                                 @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";
        model.addAttribute("products", productRepository.findAll());
        return "AdminProducts/AdminProducts";
    }

    @GetMapping("/AdminProducts/AddProduct")
    public String showAddProductForm(Model model,
                                     @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryRepository.findAll());
        return "AdminProducts/AddProduct";
    }

    @PostMapping("/AdminProducts/AddProduct")
    public String addProduct(@ModelAttribute ProductDTO productDTO,
                             @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) throws IOException {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        String fileName = null;
        if (productDTO.getImage() != null && !productDTO.getImage().isEmpty()) {
            MultipartFile file = productDTO.getImage();
            fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File uploadPath = new File(uploadProperties.getProductsDir());
            if (!uploadPath.exists()) uploadPath.mkdirs();
            file.transferTo(new File(uploadPath, fileName));
        }

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Product product = new Product(
                productDTO.getTitle(),
                productDTO.getPrice(),
                productDTO.getDescription(),
                productDTO.getQuantity(),
                fileName,
                category
        );

        productRepository.save(product);
        return "redirect:/AdminProducts";
    }

    @GetMapping("/AdminProducts/EditProduct")
    public String showEditProductForm(@RequestParam Long id, Model model,
                                      @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setCategoryId(product.getCategory().getId());

        model.addAttribute("product", dto);
        model.addAttribute("categories", categoryRepository.findAll());
        return "AdminProducts/EditProduct";
    }

    @PostMapping("/AdminProducts/update")
    public String updateProduct(@ModelAttribute ProductDTO productDTO,
                                @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) throws IOException {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        Product existingProduct = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productDTO.getId()));

        existingProduct.setTitle(productDTO.getTitle());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantity(productDTO.getQuantity());

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        existingProduct.setCategory(category);

        if (productDTO.getImage() != null && !productDTO.getImage().isEmpty()) {
            MultipartFile file = productDTO.getImage();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File uploadPath = new File(uploadProperties.getProductsDir());
            if (!uploadPath.exists()) uploadPath.mkdirs();
            file.transferTo(new File(uploadPath, fileName));
            existingProduct.setImage(fileName);
        }

        productRepository.save(existingProduct);
        return "redirect:/AdminProducts";
    }

    @GetMapping("/AdminProducts/delete/{id}")
    public String deleteProduct(@PathVariable Long id,
                                @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";
        productRepository.deleteById(id);
        return "redirect:/AdminProducts";
    }

    // =================== إدارة الأدمين ===================
    @GetMapping("/AdminAuth/AdminLogin")
    public String adminLogin(Model model) {
        model.addAttribute("admin", new Admin());
        return "AdminAuth/AdminLogin";
    }

    @PostMapping("/AdminAuth/AdminLogin")
public String doLogin(@ModelAttribute Admin admin, HttpSession session, RedirectAttributes redirectAttributes) {

    // تحقق من البريد و كلمة السر
    Optional<Admin> adminOpt = adminRepository.findByEmail(admin.getEmail())
            .filter(a -> a.getPassword().equals(admin.getPassword()));

    if (adminOpt.isPresent()) {
        // login ناجح
        Admin loggedAdmin = adminOpt.get();
        session.setAttribute("currentAdmin", loggedAdmin);  // حفظ في session

        // تحقق لو فيه redirect لصفحة معينة قبل login
        String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
        if (redirectUrl != null) {
            session.removeAttribute("redirectAfterLogin");
            return "redirect:" + redirectUrl;
        }

        // default redirect للـ dashboard
        return "redirect:/AdminDashboard";
    } else {
        // login فشل
        redirectAttributes.addFlashAttribute("error", "البريد الإلكتروني أو كلمة المرور غير صحيحة");
        return "redirect:/AdminAuth/AdminLogin";
    }
}


    @GetMapping("/AdminAuth/AdminRegister")
    public String adminRegister(Model model) {
        model.addAttribute("admin", new Admin());
        return "AdminAuth/AdminRegister";
    }

    @PostMapping("/AdminAuth/AdminRegister")
    public String doRegister(@ModelAttribute Admin admin, Model model) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            model.addAttribute("error", "البريد الإلكتروني مستخدم من قبل!");
            return "AdminAuth/AdminRegister";
        }
        adminRepository.save(admin);
        model.addAttribute("success", "تم التسجيل بنجاح! يمكنك الآن تسجيل الدخول.");
        return "AdminAuth/AdminLogin";
    }

    @GetMapping("/AdminAuth/AddAdmin")
    public String showAddAdminForm(Model model,
                                   @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";
        model.addAttribute("admin", new Admin());
        return "AdminAuth/AddAdmin";
    }

    @PostMapping("/AdminAuth/AddAdmin")
    public String addAdmin(@ModelAttribute Admin admin,
                           @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin,
                           Model model) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            model.addAttribute("error", "البريد الإلكتروني مستخدم من قبل!");
            return "AdminAuth/AddAdmin";
        }

        adminRepository.save(admin);
        model.addAttribute("success", "تم إضافة المسؤول بنجاح!");
        return "redirect:/AdminDashboard";
    }

    @GetMapping("/AdminDashboard")
    public String adminDashboard(@SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";
        return "AdminAuth/AdminDashboard";
    }

    // =================== إدارة المستخدمين ===================
    @GetMapping("/UserDetails")
    public String viewUsers(Model model,
                            @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        model.addAttribute("userList", userRepository.findAll());
        model.addAttribute("user", new User());
        return "UserDetails";
    }

    @PostMapping("/UpdateUser")
    public String updateUser(@ModelAttribute("user") User user,
                             @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        userRepository.save(user);
        return "redirect:/UserDetails";
    }

    @GetMapping("/EditUser")
    public String editUser(@RequestParam("id") Long id, Model model,
                           @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "redirect:/UserDetails";

        model.addAttribute("user", user);
        return "EditUser";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id,
                             @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";
        userRepository.deleteById(id);
        return "redirect:/UserDetails";
    }

    // =================== إدارة الطلبات ===================
    @GetMapping("/UserOrders")
    public String showUserOrders(Model model,
                                 @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        model.addAttribute("orders", orderService.getConfirmedOrders());
        return "UserOrders";
    }

    @PostMapping("/UserOrders/approve/{id}")
    public String approveOrder(@PathVariable Long id,
                               @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        orderService.approveOrder(id);
        return "redirect:/UserOrders";
    }

    @PostMapping("/UserOrders/delete/{id}")
    public String deleteOrder(@PathVariable Long id,
                              @SessionAttribute(name = "currentAdmin", required = false) Admin currentAdmin) {
        if (currentAdmin == null) return "redirect:/AdminAuth/AdminLogin";

        orderService.deleteOrder(id);
        return "redirect:/UserOrders";
    }
}
