package com.tmt.tmdt.controller;

import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.mapper.CategoryMapper;
import com.tmt.tmdt.mapper.ProductMapper;
import com.tmt.tmdt.service.CategoryService;
import com.tmt.tmdt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
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
        List<Product> products = productService.getProducts();
        model.addAttribute("products", products);
        return "home/home";
    }

    @GetMapping("category/{categoryCode}")
    public String showProductByCategory(Model model, @PathVariable String categoryCode) {

        String stringId = categoryCode
                .substring(categoryCode.lastIndexOf(".") + 1, categoryCode.length());
        Integer categoryId = Integer.valueOf(stringId);
        Category category = categoryService.getCategory(categoryId);
        List<ProductResponseDto> rs = productService.getProductsByCategoryForHome(category);
        model.addAttribute("products", rs);

        return "home/products/productsByCategory";
    }


}





