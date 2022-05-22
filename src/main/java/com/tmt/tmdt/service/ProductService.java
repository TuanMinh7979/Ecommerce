package com.tmt.tmdt.service;

import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {

    // for auto complete search
    List<String> getNamesByKw(String kw);

    // for input search by keyword
    Page<Product> getProductsByName(String name, Pageable pageable);

    Product getProductByName(String name);

    Product getProduct(Long id);

    Page<Product> getProductsByCategory(Long categoryId, Pageable pageable);

    List<Product> getProducts();

    boolean existByName(String name);

    Page<Product> getProductsByCategoryAndNameLike(Long categoryId, String name, Pageable pageable);

    Page getProducts(Pageable pageable);

    void deleteById(Long id) throws IOException;

    void deleteProducts(Long[] ids) throws IOException;

    Product getProductWithImages(Long id);


    Product add(Product product);


    Product update(Product product);

    Product updateWithChgParent(Product product);

    Product savePersistence(Product product);


    Product getProductWithImagesAndCategory(Long id);

    List<ProductResponseDto> getProductDtos();




}
