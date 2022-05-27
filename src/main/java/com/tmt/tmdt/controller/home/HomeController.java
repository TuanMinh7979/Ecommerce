package com.tmt.tmdt.controller.home;

import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.entities.pojo.FilterQuery;
import com.tmt.tmdt.mapper.ProductMapper;
import com.tmt.tmdt.repository.CategoryRepo;
import com.tmt.tmdt.service.CategoryService;
import com.tmt.tmdt.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final CategoryRepo categoryRepo;
    private final ProductMapper productMapper;
    private final FilterQuery filterQuery;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/403")
    public String permissionDenied() {
        return "admin/error/403Err";
    }


    @GetMapping("")
    public String index(Model model) {
        List<ProductResponseDto> products = productService.getProductDtos();
        model.addAttribute("products", products);
        return "home/home";
    }

    @GetMapping("category/{categoryCode}")
    public String showProductByCategory(Model model, @PathVariable String categoryCode,

                                        @RequestParam(value = "sortBy", required = false) String sortByPar,
                                        @RequestParam(value = "sortDir", required = false) String sortDirectionPar,
                                        @RequestParam(required = false) Map<String, String> allRequestParams) {

        String sortBy = sortByPar != null ? sortByPar : "price";
        String sortDirection = sortDirectionPar != null ? sortDirectionPar : "desc";

        String stringId = categoryCode
                .substring(categoryCode.lastIndexOf(".") + 1, categoryCode.length());
        Integer categoryId = Integer.valueOf(stringId);
        if (categoryId == 1 && allRequestParams.size() == 0) {
            List<ProductResponseDto> productDtos = productService.getProductDtos();
            model.addAttribute("products", productDtos);
            return "home/product/productsByCategory";
        }


        Category category = categoryService.getCategory(categoryId);
        List<Integer> queryIds = Arrays.asList(category.getChildrenIdsInView().trim().split(" "))
                .stream()
                .map(idStr -> Integer.valueOf(idStr))
                .collect(Collectors.toList());


        ArrayList<String> keyFilters = null;

        StringBuilder queryString = new StringBuilder("select * from products p ");
        allRequestParams.keySet().removeIf(key -> key.equals("sortBy"));
        allRequestParams.keySet().removeIf(key -> key.equals("sortDir"));
        keyFilters = new ArrayList<>(allRequestParams.values());


        queryString.append("where p.category_id in (");
        for (Integer idi : queryIds) {
            String idiStrToQuery = idi + ",";
            queryString.append(idiStrToQuery);
        }
        queryString = new StringBuilder(queryString.substring(0, queryString.length() - 1));
        queryString.append(") ");

        if (keyFilters != null && keyFilters.size() != 0) {

            Map<String, String> queryMap = filterQuery.getGetQueryMap();

            for (int i = 0; i < keyFilters.size(); i++) {
                String queryKey = keyFilters.get(i).trim();
                String queryWhereClause = queryMap.get(queryKey);
                queryString.append(" and ");
                queryString.append(queryWhereClause);
            }
            model.addAttribute("categoryFilter", category.getFilter());

        }

        queryString.append("order by " + sortBy + " " + sortDirection);
        queryString.append(";");
        List<Product> products = entityManager.createNativeQuery(
                        queryString.toString(),
                        Product.class)
                .getResultList();
        List<ProductResponseDto> productDtos = products.stream().map(productMapper::toProductResponseDto).collect(Collectors.toList());


        model.addAttribute("products", productDtos);

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

    @GetMapping("cart")
    public String showCart() {
        return "home/cart";
    }



    @PostMapping("product-by-name")
    @ResponseBody
    public String showProductDetailUrlByName(@RequestParam("name") String name) {
        String code = productService.getCodeByName(name);
        String newUrl = "/product/" + code;

        return newUrl;
    }

    @GetMapping("product")
    public String showProductResDtoDetailByName(Model model, @RequestParam("name") String name) {
        List<ProductResponseDto> rs = productService.getProductResDtosByNameLike(name);
        model.addAttribute("products", rs);
        return "home/home";
    }


}
