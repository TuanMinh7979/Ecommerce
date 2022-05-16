package com.tmt.tmdt.controller.admin;

import com.tmt.tmdt.dto.request.FileRequestDto;
import com.tmt.tmdt.dto.response.ViewResponseApi;
import com.tmt.tmdt.entities.Image;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.service.CategoryService;
import com.tmt.tmdt.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;


    @GetMapping("")
    public String index(Model model) {

        model.addAttribute("categoriesForFilter", categoryService.getCategoriesInHierarchicalFromRoot());
        return "admin/product/index";
    }

    @GetMapping("add")
    public String showAddForm(Model model) {
        Product product = new Product();

        model.addAttribute("product", product);

//       model.addAttribute("categoriesForForm", categoryService.getCategoriesInHierarchicalFromRoot());
        return "admin/product/add";

    }

    @GetMapping("api/viewApi")
    @ResponseBody
    public ViewResponseApi<List<Product>> getProducts(
            @RequestParam(name = "page", required = false) String pageParam,
            @RequestParam(name = "limit", required = false) String limitParam,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "sortDirection", required = false) String sortDirection,
            @RequestParam(name = "searchNameTerm", required = false) String searchNameTerm,
            @RequestParam(name = "category", required = false) String categoryIdParam) {


        String sortField = sortBy == null ? "id" : sortBy;
        Sort sort = (sortDirection == null || sortDirection.equals("asc")) ? Sort.by(Sort.Direction.ASC, sortField)
                : Sort.by(Sort.Direction.DESC, sortField);
        int page = pageParam == null ? 0 : Integer.parseInt(pageParam) - 1;
        int limit = limitParam == null ? 5 : Integer.parseInt(limitParam);

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page productPage = null;

        if (categoryIdParam != null && !categoryIdParam.isEmpty() && Long.parseLong(categoryIdParam) != 0) {
            //get product by category
            Long categoryId = Long.parseLong(categoryIdParam);
            if (searchNameTerm != null && !searchNameTerm.isEmpty()) {
                productPage = productService.getProductsByCategoryAndNameLike(categoryId, searchNameTerm, pageable);
            } else {
                productPage = productService.getProductsByCategory(categoryId, pageable);
            }


        } else if (searchNameTerm != null && !searchNameTerm.isEmpty()) {

            productPage = productService.getProductsByName(searchNameTerm, pageable);
        } else {
            productPage = productService.getProducts(pageable);
        }
        List data = productPage.getContent();
        int totalPage = productPage.getTotalPages();


        return new ViewResponseApi<>(totalPage, data);
    }


    @PostMapping("add")
//    @Transactional
    public String add(Model model,
                      @RequestParam("file") FileRequestDto mainImageDto,
                      @RequestParam(value = "mainColor", required = false) String mainColor,
                      @RequestParam(value = "files", required = false) List<FileRequestDto> extraImageDtos,
                      @RequestParam(value = "extraColor", required = false) List<String> extraColors,
                      @Valid @ModelAttribute("product") Product product, BindingResult result) {
        if (productService.existByName(product.getName())) {

            result.rejectValue("name", "nameIsExist");
        }
        if (!result.hasErrors()) {
            try {

                product.setAtbs(product.getCategory().getAtbs());
                productService.add(product, mainImageDto, mainColor, extraImageDtos, extraColors);
                return "redirect:/admin/product";
            } catch (IOException e) {
                model.addAttribute("message", "Can not read your file, try again please!");

                return "admin/product/add";
            }
        }
        //handle loi bindding
        return "admin/product/add";

    }


    @PostMapping(value = "update")
    public String update(Model model,
                         @RequestParam("file") FileRequestDto mainImageDto,
                         @RequestParam(value = "mainColor", required = false) String mainColor,
                         @RequestParam(value = "files", required = false) List<FileRequestDto> extraImageDtos,
                         @RequestParam(value = "extraColor", required = false) List<String> extraColors,
                         @RequestParam("delImageIds") String delImageIds,
                         @RequestParam(value = "flags", required = false) String flags,
                         @Valid @ModelAttribute("product") Product product,
                         BindingResult result) throws IOException {


        System.out.println("*******************************:");
        System.out.println(flags);

        if (!result.hasErrors()) {
            if (flags == null) {
                System.out.println("NLLLLLLLLLLLLLLLLLLL");
                productService.save(product);
            } else {
                System.out.println("NNNNNNNNNNNNNNOOOOOOOOOOOOOOOTT NULL");

                productService.update(product, mainImageDto, mainColor, extraImageDtos, extraColors, delImageIds, flags);
            }
            return "redirect:/admin/product";
        }
        return "admin/product/edit";
    }


    @GetMapping("edit/{idx}")
    //rest api : showUpdateForm , showAddForm => getCategory(get)(just for update)
    public String showUpdateForm(Model model, @PathVariable("idx") String idx) {

        Product product = null;
        try {
            //Catch casting exception
            Long id = Long.parseLong(idx);
            product = productService.getProductWithImages(id);


        } catch (NumberFormatException e) {
            e.printStackTrace();
            product = productService.getProductByName(idx);
            product = productService.getProductWithImages(product.getId());

        }
        //other exception will be handled in service
        model.addAttribute("product", product);
        List<Image> extraImages = new ArrayList<>();
        for (Image imagei : product.getImages()) {
            if (!imagei.isMain()) {
                extraImages.add(imagei);
            } else {
                model.addAttribute("mainImage", imagei);
            }
        }

        model.addAttribute("images", extraImages.stream()
                .sorted(Comparator.comparingLong(Image::getId))
                .collect(Collectors.toList()));
//                .filter(img -> img.getIsMain() == false).collect(Collectors.toSet()));
        return "admin/product/edit";

    }

    @PostMapping("api/delete/{id}")
    //call with ajax
    public ResponseEntity<Long> deleteProduct(@PathVariable("id") Long id) throws IOException {

        productService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("api/delete")
    public ResponseEntity<Long[]> deleteProducts(@RequestBody Long[] ids) {

        productService.deleteProducts(ids);
        return new ResponseEntity<>(ids, HttpStatus.OK);

    }


//    FOR ATTRIBUTE

    @GetMapping("api/{id}/attributes")
    @ResponseBody
    public String getAttributesByProductId(@PathVariable("id") Long id) {
        return productService.getProduct(id).getAtbs();
    }

    @PostMapping("api/{id}/attributes/update")
    @ResponseBody
    public String updateAttributes(@PathVariable("id") Long id, @RequestBody String newAttributes) {


        Product product = productService.getProduct(id);

        product.setAtbs(newAttributes);


        productService.save(product);

        return newAttributes;

    }


}




