package com.tmt.tmdt.controller;

import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.mapper.ProductMapper;
import com.tmt.tmdt.service.CategoryService;
import com.tmt.tmdt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;


    @GetMapping("")
    public String index(Model model) {

        model.addAttribute("categoriesForForm", categoryService.getCategoriesInHierarchicalFromRootWithOut(2));
        return "home/home";
    }

    @GetMapping("/{categoryCode}")
    public String showProductByCategory(Model model, @PathVariable String categoryCode) {

        String stirngId = categoryCode
                .substring(categoryCode.lastIndexOf(".") + 1, categoryCode.length());

        Integer categoryId = Integer.valueOf(stirngId);
        List<Product> products = productService.getProductsByCategory(categoryId);
        model.addAttribute("products", products.stream()
                .map(productMapper::toProductResponseDto)
                .collect(Collectors.toList()));

        return "home/products/productsByCategory";
    }


}





