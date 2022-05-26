package com.tmt.tmdt.controller.admin;

import com.tmt.tmdt.dto.request.ImageRequestDto;
import com.tmt.tmdt.dto.response.ViewResponseApi;
import com.tmt.tmdt.entities.Image;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.mapper.ImageMapper;
import com.tmt.tmdt.service.CategoryService;
import com.tmt.tmdt.service.ImageService;
import com.tmt.tmdt.service.ProductService;
import com.tmt.tmdt.service.UploadService;
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
    private final ImageMapper imageMapper;
    private final UploadService uploadService;
    private final ImageService imageService;


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
    public String add(Model model, @Valid @ModelAttribute("product") Product product, BindingResult result) {
        if (productService.existByName(product.getName())) {

            result.rejectValue("name", "nameIsExist");
        }
        if (!result.hasErrors()) {

            product.setAtbs(product.getCategory().getAtbs());
            productService.add(product);
            return "redirect:/admin/product";

        }
        //handle loi bindding
        return "admin/product/add";

    }


    @PostMapping(value = "update")
    public String update(Model model,
                         @Valid @ModelAttribute("product") Product product,
                         BindingResult result) throws IOException {

        if (!result.hasErrors()) {

            productService.update(product);

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
            product = productService.getProduct(id);

        } catch (NumberFormatException e) {
            //for find by name
            e.printStackTrace();
            product = productService.getProductByName(idx);
        }
        model.addAttribute("product", product);

        return "admin/product/edit";
    }

    @GetMapping("edit/{id}/manage-image")
    public String editImages(Model model, @PathVariable Long id) {
        Product product = productService.getProductWithImages(id);
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

        return "admin/product/product_images";
    }

    @PostMapping("api/delete/{id}")
    //call with ajax
    public ResponseEntity<Long> deleteProduct(@PathVariable("id") Long id) throws IOException {

        productService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("api/delete")
    public ResponseEntity<Long[]> deleteProducts(@RequestBody Long[] ids) throws IOException {
        productService.deleteProducts(ids);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }
//    FOR ATTRIBUTE

    @GetMapping("api/{id}/attributes")
    @ResponseBody
    public String getAttributesByProductId(@PathVariable("id") Long id) {
        return productService.getProduct(id).getAtbs();
    }

    @PostMapping("edit/{id}/manage-image/add")
    @ResponseBody
    public String addProductImages(Model model, @PathVariable Long id, @ModelAttribute ImageRequestDto imageDto) throws IOException {


        imageDto.setUploadRs(uploadService.simpleUpload(imageDto.getFile()));
        Image image = imageMapper.toModel(imageDto);
        Product product = productService.getProduct(id);
        image.setProduct(product);
        Image savedImage = imageService.save(image);
        if (savedImage.isMain()) {
            product.setMainImageLink(savedImage.getLink());
        }
        productService.savePersistence(product);
        return "";
    }

    @PostMapping("edit/{id}/manage-image/update/{imgId}")
    @ResponseBody
    public String updateProductImages(Model model, @PathVariable Long id, @PathVariable Long imgId, @ModelAttribute ImageRequestDto imageDto) {
        Image oldImage = imageService.getImage(imgId);
        //just update some attribute ,image file alway null when save.
        //if want
        if (oldImage.getColor() != imageDto.getColor()) {
            oldImage.setColor(imageDto.getColor());

            imageService.save(oldImage);
        }

        return "";

    }

    @PostMapping("edit/{id}/manage-image/delete")
    @ResponseBody
    public String deleteProductImagesByIds(@PathVariable Long id, @RequestParam("delImageIds") String delImageIds) throws IOException {


        Product product = productService.getProduct(id);
        delImageIds = delImageIds.trim();
        List<String> strIds = Arrays.asList(delImageIds.split(" "));
        Set<Long> ids = strIds.stream().map(Long::valueOf).collect(Collectors.toSet());
        //remove image from database (orphan removeal and deleit in cloud)
        for (Long idToDel : ids) {
            if (imageService.getImage(idToDel).isMain()) {
                product.setMainImageLink(product.defaultImage());
                productService.savePersistence(product);
            }
            imageService.deleteById(idToDel);
        }
        return "";

    }


}


//







