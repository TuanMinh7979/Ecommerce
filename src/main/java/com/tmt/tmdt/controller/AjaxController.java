package com.tmt.tmdt.controller;

import com.tmt.tmdt.dto.response.CategoryResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.pojo.CatWithNofDirectSubCat;
import com.tmt.tmdt.repository.CategoryRepo;
import com.tmt.tmdt.repository.ProductRepo;
import com.tmt.tmdt.service.ImageService;
import com.tmt.tmdt.service.ProductService;
import com.tmt.tmdt.service.RoleService;
import com.tmt.tmdt.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("ajax")
public class AjaxController {
    private final ProductService productService;
    //    private final CategoryService categoryService;
    private final CategoryServiceImpl categoryService;
    private final RoleService roleService;
    private final ImageService imageService;

    private final ProductRepo productRepo;
    private final CategoryRepo catRepo;


    @GetMapping("autocomplete-search/product")
    public List<String> getProductNamesByKw(@RequestParam("term") String kw) {
        return productService.getNamesByKw(kw);
    }

    @GetMapping("autocomplete-search/category")
    public List<String> getCategoryNamesByKw(@RequestParam("term") String kw) {
        return categoryService.getCategoryNamesByKw(kw);
    }

    @GetMapping("autocomplete-search/role")
    public List<String> getRoleNamesByKw(@RequestParam("term") String kw) {
        return roleService.getRoleNamesByKw(kw);
    }

    @GetMapping("/menudata")

    public List<CategoryResponseDto> getMenu() {
        List<CategoryResponseDto> categories = categoryService.getCategoryResponseDtos();
        categories.sort(Comparator.comparing(CategoryResponseDto::getId));
        categories.remove(1);
        return categories;
    }

    @GetMapping("/categoryHierarchical")
    public List<Category> getCategoriesHierarchical() {
        return categoryService.getCategoriesInHierarchicalFromRootWithOut(2);
    }

    @GetMapping("/test")
    public List<CatWithNofDirectSubCat> gettest() {

        return categoryService.getCategoriesWithNofDirectSubCat();
    }


}
