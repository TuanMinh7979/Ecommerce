package com.tmt.tmdt.service;

import com.tmt.tmdt.dto.request.FileRequestDto;
import com.tmt.tmdt.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {

    //for auto complete search
    List<String> getNamesByKw(String kw);

    //for input search by keyword
    Page<Product> getProductsByName(String name, Pageable pageable);

    List<Product> getProductsByName(String name);

    Product getProductByName(String name);

    Product getProduct(Long id);

    Page<Product> getProductsByCategory(Long categoryId, Pageable pageable);

    List<Product> getProducts();

    boolean existByName(String name);

    Page<Product> getProductsByCategoryAndNameLike(Long categoryId, String name, Pageable pageable);

    Page getProducts(Pageable pageable);

    void deleteById(Long id);

    void deleteProducts(Long[] ids);

    Product getProductWithImages(Long id);


    List<Product> getProductsByCategory(Integer categoryId);

    Product add(Product product, FileRequestDto fileRequestDto, List<FileRequestDto> fileRequestDtos) throws IOException;

    Product update(Product product, FileRequestDto file, List<FileRequestDto> files, String ids) throws IOException;

    Product save(Product product);
}
