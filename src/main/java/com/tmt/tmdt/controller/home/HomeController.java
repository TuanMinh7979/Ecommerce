package com.tmt.tmdt.controller.home;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

                                        @RequestParam(value = "sortBy", required = false) String sortBy,
                                        @RequestParam(value = "sortDir", required = false) String sortDir,
                                        @RequestParam(required = false) Map<String, String> allRequestParams) {

        String stringId = categoryCode
                .substring(categoryCode.lastIndexOf(".") + 1, categoryCode.length());
        Integer categoryId = Integer.valueOf(stringId);
        if (allRequestParams != null && allRequestParams.size() != 0) {
            List<Integer> categoryIdsToQuery = productService.getListIdToQuery(categoryId);

            ArrayList<String> keyFilters = null;

            StringBuilder queryString = new StringBuilder("select * from products p ");
            allRequestParams.keySet().removeIf(key -> key.equals("sortBy"));
            allRequestParams.keySet().removeIf(key -> key.equals("sortDir"));
            keyFilters = new ArrayList<String>(allRequestParams.values());


            queryString.append("where p.category_id in (") ;
            for(Integer idi: categoryIdsToQuery){
                String idiStrToQuery= idi+",";
                queryString.append(idiStrToQuery);
            }
            queryString= new StringBuilder(queryString.substring(0, queryString.length() - 1));
            queryString.append(") ");

            if (keyFilters != null && keyFilters.size() != 0) {

                Map<String, String> queryMap = filter.getGetQueryMap();

                for (int i = 0; i < keyFilters.size(); i++) {
                    String queryKey = keyFilters.get(i).trim();
                    String queryWhereClause = queryMap.get(queryKey);
                    queryString.append(" and ");
                    queryString.append(queryWhereClause);
                }

            }

            if (sortBy == null && sortDir == null) {
                queryString.append(" order by price asc");
            } else if (sortBy != null && sortDir != null) {
                queryString.append(" order by " + sortBy + " " + sortDir);
            } else if (sortBy != null) {
                queryString.append(" order by " + sortBy + " asc");
            } else {
                queryString.append(" order by price" + " " + sortDir);
            }
            queryString.append(";");


            List<Product> products = entityManager.createNativeQuery(
                            queryString.toString(),
                            Product.class)
                    .getResultList();
            List<ProductResponseDto> productDtos =products.stream().map(productMapper::toProductResponseDto).collect(Collectors.toList());


            model.addAttribute("products", productDtos);
            return "home/product/productsByCategory";

        }

        Category category = categoryService.getCategory(categoryId);
        List<ProductResponseDto> rs = productService.getProductDtosByCategory(
                category);

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
