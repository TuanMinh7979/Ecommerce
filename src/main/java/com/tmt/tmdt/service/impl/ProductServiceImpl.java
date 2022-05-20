package com.tmt.tmdt.service.impl;

import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Image;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.exception.ResourceNotFoundException;
import com.tmt.tmdt.repository.CategoryRepo;
import com.tmt.tmdt.repository.ProductRepo;
import com.tmt.tmdt.service.ImageService;
import com.tmt.tmdt.service.ProductService;
import com.tmt.tmdt.util.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableTransactionManagement

public class ProductServiceImpl implements ProductService {
    private final ImageService imageService;
    private final ProductRepo productRepo;


    private final CategoryRepo categoryRepo;

    @Override
    public Product getProduct(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    @Override
    public List<String> getNamesByKw(String kw) {

        List<String> productNames = productRepo.getNamesByKw(kw);

        return productNames;
    }

    @Override
    public boolean existByName(String name) {
        return productRepo.existsByName(name);
    }

    @Override
    public Page<Product> getProductsByName(String name, Pageable pageable) {
        return productRepo.getProductsByName(name, pageable);
    }

    @Override
    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepo.getProductsByCategory(categoryId, pageable);
    }

    @Override
    public Product getProductByName(String name) {
        return productRepo.getProductByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + name + " not found"));

    }

    @Override
    public Page<Product> getProductsByCategoryAndNameLike(Long categoryId, String name, Pageable pageable) {
        return productRepo.getProductsByCategoryAndNameLike(categoryId, name, pageable);
    }

    @Override
    public Page getProducts(Pageable pageable) {
        return productRepo.findAll(pageable);
    }

    @Override
    public Product add(Product product) {
        Product productSaved = productRepo.save(product);
        Category category = productSaved.getCategory();
        category.setNumOfDirectProduct(category.getNumOfDirectProduct() + 1);
        categoryRepo.save(category);
        productSaved.setCode(TextUtil.generateCode(product.getName(), product.getId()));
        return productRepo.save(productSaved);
    }

    @Override
    public Product update(Product product) {
        product.setCode(TextUtil.generateCode(product.getName(), product.getId()));
        return productRepo.save(product);
    }

    //Rare case: update with changed parent category
    @Override
    public Product updateWithChgParent(Product product) {

        Category oldCategory = categoryRepo.findById(product.getCategory().getId()).
                orElseThrow(() -> new ResourceNotFoundException("Category of product not found"));
        oldCategory.setNumOfDirectProduct((oldCategory.getNumOfDirectProduct() - 1) > 0
                ? (oldCategory.getNumOfDirectProduct() - 1)
                : 0);
        categoryRepo.save(oldCategory);
        Category newCategory = product.getCategory();
        newCategory.setNumOfDirectProduct(newCategory.getNumOfDirectProduct() + 1);
        categoryRepo.save(newCategory);

        product.setCode(TextUtil.generateCode(product.getName(), product.getId()));
        return productRepo.save(product);

    }

    @Override
    public Product savePersistence(Product product) {
        return productRepo.save(product);
    }

    @Override
    public void deleteById(Long id) throws IOException {
        // sql error
        // return null to sign that error happened
        Product product = getProductWithImagesAndCategory(id);

        Set<Image> images = product.getImages();
        for (Image image : images) {
            imageService.deleteById(image.getId());
        }

        Category category = product.getCategory();
        category.setNumOfDirectProduct((category.getNumOfDirectProduct() - 1) > 0 ? (category.getNumOfDirectProduct() - 1) : 0);
        categoryRepo.save(category);
        productRepo.deleteById(id);

    }

    @Override
    public void deleteProducts(Long[] ids) throws IOException {
        for (Long id : ids) {
            Product product = getProductWithImagesAndCategory(id);

            Set<Image> images = product.getImages();
            for (Image image : images) {
                imageService.deleteById(image.getId());
            }

            Category category = product.getCategory();
            category.setNumOfDirectProduct((category.getNumOfDirectProduct() - 1) > 0 ? (category.getNumOfDirectProduct() - 1) : 0);
            categoryRepo.save(category);
            productRepo.deleteById(id);
        }
    }

    @Override
    public Product getProductWithImages(Long id) {

        Product productWithImages = productRepo.getProductWithImages(id)
                .orElseThrow(() -> new ResourceNotFoundException("product with id " + id + " is not found"));
        return productWithImages;
    }

    @Override
    public Product getProductWithImagesAndCategory(Long id) {
        return productRepo.getProductWithImagesAndCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " is not found"));
    }

    @Override
    public List<ProductResponseDto> getProductDtos() {
        return productRepo.getProductDtos();
    }


}
