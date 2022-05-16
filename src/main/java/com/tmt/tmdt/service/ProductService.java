package com.tmt.tmdt.service;

import com.tmt.tmdt.dto.request.FileRequestDto;
import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.entities.Category;
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


    Product add(Product product, FileRequestDto fileRequestDto, String mainColor,
                List<FileRequestDto> fileRequestDtos, List<String> extraColors)
            throws IOException;

    Product update(Product product, FileRequestDto file, String mainColor,
                   List<FileRequestDto> files, List<String> extraColors,
                   String ids, String flags) throws IOException;

    Product save(Product product);

    List<ProductResponseDto> getProductDtosByCategory(Category category);

    Product getProductWithImagesAndCategory(Long id);

    List<ProductResponseDto> getProductDtos();

    List<Integer> getListIdToQuery(Integer id);

}
