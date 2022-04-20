package com.tmt.tmdt.service.impl;

import com.cloudinary.Cloudinary;
import com.tmt.tmdt.dto.request.FileRequestDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Image;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.exception.ResourceNotFoundException;
import com.tmt.tmdt.mapper.ImageMapper;
import com.tmt.tmdt.repository.ProductRepo;
import com.tmt.tmdt.service.ImageService;
import com.tmt.tmdt.service.ProductService;
import com.tmt.tmdt.service.UploadService;
import com.tmt.tmdt.util.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableTransactionManagement

public class ProductServiceImpl implements ProductService {
    private final ImageService imageService;
    private final Cloudinary cloudinary;
    private final ProductRepo productRepo;

    private final UploadService uploadService;
    private final ImageMapper imageMapper;


    @Override
    public Product getProduct(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product with id " + id + " not found")
                );
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
    public List<Product> getProductsByName(String name) {
        return productRepo.getProductsByName(name);
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
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product with id " + name + " not found"));

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

    public Product add(Product product, FileRequestDto fileRequestDto, List<FileRequestDto> fileRequestDtos) throws IOException {
        Category category = product.getCategory();
        product.setAttributes(category.getAttributes());
        Product productSaved = save(product);
        productSaved.setCode(TextUtil.generateCode(product.getName(), product.getId()));
        //relation should work in persistence


        if (!fileRequestDto.getFile().isEmpty()) {
            //save main image
            fileRequestDto.setUploadRs(uploadService.simpleUpload(fileRequestDto.getFile()));
            Image mainImage = imageMapper.toModel(fileRequestDto);
            mainImage.setProduct(productSaved);
            mainImage.setMain(true);
            Image savedMainImage = imageService.save(mainImage);

            productSaved.setMainImageLink(savedMainImage.getLink());


        }
        //extra image is a option
        if (fileRequestDtos != null) {
            for (FileRequestDto extraImagei : fileRequestDtos) {
                if (!extraImagei.getFile().isEmpty()) {
                    extraImagei.setUploadRs(uploadService.simpleUpload(extraImagei.getFile()));
                    Image extraImage = imageMapper.toModel(extraImagei);
                    extraImage.setProduct(productSaved);
                    //relation should work in persistence or -> not save trasient before flush
                    imageService.save(extraImage);
                }
            }
        }

        return save(productSaved);
    }

    @Override
    public Product update(Product product, FileRequestDto fileRequestDto, List<FileRequestDto> fileRequestDtos, String delImageIds) throws IOException {
        delImageIds = delImageIds.trim();

        if (delImageIds != null && !delImageIds.isEmpty()) {
            delImageIds = delImageIds.trim();
            List<String> strIds = Arrays.asList(delImageIds.split(" "));
            Set<Long> ids = strIds.stream().map(Long::valueOf).collect(Collectors.toSet());
            //remove image from database (orphan removeal and deleit in cloud)
            for (Long idToDel : ids) {
                if (imageService.getImage(idToDel).isMain()) {
                    product.setMainImageLink(product.defaultImage());
                }
                imageService.deleteById(idToDel);
            }

        }
        if (!fileRequestDto.getFile().isEmpty()) {
            //save main image
            fileRequestDto.setUploadRs(uploadService.simpleUpload(fileRequestDto.getFile()));
            Image mainImage = imageMapper.toModel(fileRequestDto);
            mainImage.setProduct(product);
            mainImage.setMain(true);
            Image savedMainImage = imageService.save(mainImage);
            product.setMainImageLink(savedMainImage.getLink());
        }

        if (fileRequestDtos != null) {
            for (FileRequestDto extraImagei : fileRequestDtos) {
                if (!extraImagei.getFile().isEmpty()) {
                    extraImagei.setUploadRs(uploadService.simpleUpload(extraImagei.getFile()));
                    Image extraImage = imageMapper.toModel(extraImagei);
                    extraImage.setProduct(product);
                    imageService.save(extraImage);
                }
            }
        }

        product.setCode(TextUtil.generateCode(product.getName(), product.getId()));
        return productRepo.save(product);
    }

    @Override
    public Product save(Product product) {

        //main use for case not change image
        //and must be handle for all case that persist to db
        //use for ,which use,  how often , performance


        return productRepo.save(product);

    }


    @Override
    public void deleteById(Long id) {
        //sql error
        //return null to sign that error happened
        productRepo.deleteById(id);

    }

    @Override
    public void deleteProducts(Long[] ids) {
        for (Long id : ids) {
            productRepo.deleteById(id);
        }
    }


    @Override
    public Product getProductWithImages(Long id) {
//        Product product = getProduct(id);
        Product productWithImages = productRepo.getProductWithImages(id);
        return productWithImages;
    }

    @Override
    public List<Product> getProductsByCategory(Integer categoryId) {
        return productRepo.getProductsByCategory(categoryId);
    }


}
