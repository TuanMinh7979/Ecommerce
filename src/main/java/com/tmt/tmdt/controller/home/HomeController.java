package com.tmt.tmdt.controller.home;

import java.util.*;
import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.entities.pojo.Filter;
import com.tmt.tmdt.mapper.ProductMapper;
import com.tmt.tmdt.service.CategoryService;
import com.tmt.tmdt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceContexts;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final Filter filter;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("")
    public String index(Model model) {
        List<ProductResponseDto> products = productService.getProductDtos();
        model.addAttribute("products", products);
        return "home/home";
    }

    @GetMapping("category/{categoryCode}")
    public String showProductByCategory(Model model, @PathVariable String categoryCode,
            @RequestParam(required = false) Map<String, String> allRequestParams) {

        if (allRequestParams.size() != 0) {

            ArrayList<String> keyFilters = new ArrayList<String>(allRequestParams.values());
            Map<String, String> queryMap = filter.getGetQueryMap();
            String queryString = "select * from products p where ";
            for (int i = 0; i < keyFilters.size(); i++) {
                String queryKey = keyFilters.get(i).trim();
                String queryWhereClause = queryMap.get(queryKey);
                queryString += " and ";
                queryString += queryWhereClause;
            }
            queryString = queryString.replaceFirst(" and ", " ");

            System.out.println(queryString);
            List<Product> products = entityManager
                    .createNativeQuery(
                            queryString,
                            Product.class)
                    .getResultList();

            model.addAttribute("products", products);
            return "home/product/productsByCategory";

        }
        String stringId = categoryCode
                .substring(categoryCode.lastIndexOf(".") + 1, categoryCode.length());
        Integer categoryId = Integer.valueOf(stringId);
        Category category = categoryService.getCategory(categoryId);
        List<ProductResponseDto> rs = productService.getProductDtosByCategory(category);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("products", rs);

        return "home/product/productsByCategory";
    }

    @GetMapping("product/{productCode}")
    public String showProductDetail(Model model, @PathVariable("productCode") String productCode) {
        String stringId = productCode.substring(productCode.lastIndexOf(".") + 1);
        Long productId = Long.valueOf(stringId);
        Product product = productService.getProductWithImages(productId);

        model.addAttribute("product", product);
        return "home/product/productDetail";

    }

}
