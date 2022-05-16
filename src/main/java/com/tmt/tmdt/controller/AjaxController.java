package com.tmt.tmdt.controller;

import com.tmt.tmdt.dto.response.CategoryResponseDto;
import com.tmt.tmdt.dto.response.ClientFilterResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Image;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.entities.pojo.Filter;
import com.tmt.tmdt.repository.CategoryRepo;
import com.tmt.tmdt.repository.ProductRepo;
import com.tmt.tmdt.service.ImageService;
import com.tmt.tmdt.service.ProductService;
import com.tmt.tmdt.service.RoleService;
import com.tmt.tmdt.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("ajax")
public class AjaxController {
    // this class for common component with server data use for many page
    private final ProductService productService;

    private final CategoryServiceImpl categoryService;
    private final CategoryRepo categoryRepo;

    private final RoleService roleService;

    private final Filter filter;


    @PostMapping("jsonAdd")
    @ResponseBody
    public ResponseEntity<Category> jsonAdd(@RequestBody Category category) {
        Category savedCategory = categoryRepo.save(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.OK);
    }

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

    // for home page
    @GetMapping("/menudata")
    public List<CategoryResponseDto> getMenu() {
        List<CategoryResponseDto> categories = categoryService.getCategoryResponseDtos();
        categories.sort(Comparator.comparing(CategoryResponseDto::getId));
        categories.remove(1);
        return categories;
    }

    @GetMapping("/product/{id}/images")
    public List<String> getImageLinks(@PathVariable("id") Long id) {
        Product product = productService.getProductWithImages(id);
        Set<Image> images = product.getImages();
        return images.stream().map(img -> img.getLink()).collect(Collectors.toList());
    }

    // for admin page
    @GetMapping("/hierarchical-category")
    public List<Category> getCategoriesHierarchical() {
        // can not map to dto because all category is detacted
        return categoryService.getCategoriesInHierarchicalFromRoot();
    }

    @GetMapping("product/{id}/attributes")

    public String getAttributesByProductId(@PathVariable("id") Long id) {
        return productService.getProduct(id).getAtbs();
    }

    @GetMapping("category/{id}/filter")
    public String getFilterValue(@PathVariable("id") Integer id) {
        return categoryService.getCategory(id).getFilter();

    }

    @PostMapping("filter/ui-opt-name")

    public Map<String, String> getUiOptNameMap(@RequestBody List<String> filterAtbs) {
        Map<String, String> ori = filter.getUiOptName();
        Map<String, String> kvAtbUiname = new HashMap<>();
        for (String atbName : filterAtbs) {
            kvAtbUiname.put(atbName, ori.get(atbName));
        }

        return kvAtbUiname;
    }

    @PostMapping("filter/selected-tag-value")
    @ResponseBody
    public List<ClientFilterResponseDto> getSelectedTagValue(
            @RequestBody Map<String, String> urlAtbNameOptionCodeMap) {
        Map<String, String> atbUinameMap = filter.getUiOptName();
        List<String> atbNames = new ArrayList<>(urlAtbNameOptionCodeMap.keySet());
        List<String> optionCodes = new ArrayList<String>(urlAtbNameOptionCodeMap.values());

        List<Map<String, String>> listMap = filter.getAllMap();
        List<ClientFilterResponseDto> forClientSelectedTags = new ArrayList<>();

        for (int i = 0; i < optionCodes.size(); i++) {
            for (Map<String, String> mapI : listMap) {

                String tagValue = mapI.get(optionCodes.get(i));
                if (tagValue != null) {
                    ClientFilterResponseDto dto = new ClientFilterResponseDto(
                            optionCodes.get(i),
                            atbUinameMap.get(atbNames.get(i)),
                            tagValue);
                    forClientSelectedTags.add(dto);

                    break;
                }
            }
        }
        return forClientSelectedTags;

    }

}
