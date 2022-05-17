package com.tmt.tmdt.service.impl;

import com.tmt.tmdt.dto.request.FileRequestDto;
import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Image;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.exception.ResourceNotFoundException;
import com.tmt.tmdt.mapper.ImageMapper;
import com.tmt.tmdt.mapper.ProductMapper;
import com.tmt.tmdt.repository.CategoryRepo;
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

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableTransactionManagement

public class ProductServiceImpl implements ProductService {
    private final ImageService imageService;
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final UploadService uploadService;
    private final ImageMapper imageMapper;

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
    public Product add(Product product, FileRequestDto fileRequestDto, String mainColor,
                       List<FileRequestDto> fileRequestDtos, List<String> extraColors)
            throws IOException {
        Product productSaved = save(product);
        // relation should work in persistence

        if (!fileRequestDto.getFile().isEmpty()) {
            // save main image
            fileRequestDto.setUploadRs(uploadService.simpleUpload(fileRequestDto.getFile()));
            Image mainImage = imageMapper.toModel(fileRequestDto);
            mainImage.setProduct(productSaved);

            mainImage.setColor(mainColor);

            mainImage.setMain(true);
            Image savedMainImage = imageService.save(mainImage);

            productSaved.setMainImageLink(savedMainImage.getLink());

        }
        // extra image is a option
        if (fileRequestDtos != null) {
            for (int i = 0; i < fileRequestDtos.size(); i++) {
                FileRequestDto extraImagei = fileRequestDtos.get(i);
                String extraColori = extraColors.get(i);
                if (!extraImagei.getFile().isEmpty()) {
                    extraImagei.setUploadRs(uploadService.simpleUpload(extraImagei.getFile()));
                    Image extraImage = imageMapper.toModel(extraImagei);

                    extraImage.setColor(extraColori);
                    extraImage.setProduct(productSaved);
                    // relation should work in persistence or -> not save trasient before flush
                    imageService.save(extraImage);
                }
            }
        }

        return save(productSaved);
    }

    @Override
    public Product update(Product product, FileRequestDto fileRequestDto, String mainColor,
                          List<FileRequestDto> fileRequestDtos, List<String> extraColors,
                          String delImageIds, String flags) throws IOException {

        delImageIds = delImageIds.trim();
        //first: update all (delete + add new)
        //second: loop if color != old color(old exist color img  or new added img(default: no color) )


        if (delImageIds != null && !delImageIds.isEmpty()) {
            delImageIds = delImageIds.trim();
            List<String> strIds = Arrays.asList(delImageIds.split(" "));
            Set<Long> ids = strIds.stream().map(Long::valueOf).collect(Collectors.toSet());
            // remove image from database (orphan removeal and deleit in cloud)
            for (Long idToDel : ids) {
                if (imageService.getImage(idToDel).isMain()) {
                    product.setMainImageLink(product.defaultImage());
                }
                imageService.deleteById(idToDel);
            }

        }
        //main alway != null(alway have a image for product)
        if (!fileRequestDto.getFile().isEmpty()) {
            //if no change main image (file ="")-> not exe this
            fileRequestDto.setUploadRs(uploadService.simpleUpload(fileRequestDto.getFile()));
            Image mainImage = imageMapper.toModel(fileRequestDto);
            mainImage.setProduct(product);
            mainImage.setMain(true);


            mainImage.setColor(mainColor);
            Image savedMainImage = imageService.save(mainImage);
            product.setMainImageLink(savedMainImage.getLink());
        }

        if (fileRequestDtos != null) {
            for (int i = 0; i < fileRequestDtos.size(); i++) {
                FileRequestDto extraImagei = fileRequestDtos.get(i);
                if (!extraImagei.getFile().isEmpty()) {
                    //if no change any extra img (filei = "")-> not exe this
                    String extraColori = extraColors.get(i);
                    extraImagei.setUploadRs(uploadService.simpleUpload(extraImagei.getFile()));
                    Image extraImage = imageMapper.toModel(extraImagei);

                    extraImage.setColor(extraColori);
                    extraImage.setProduct(product);
                    imageService.save(extraImage);

                }
            }
        }


        Category oldCategory = categoryRepo.getCategoryByProductId(product.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category of product id : " + product.getId() + " not found"));
        if (oldCategory.getId() != product.getCategory().getId()) {
            Category newCategory = product.getCategory();
            // change product category

            oldCategory.setNumOfDirectProduct(
                    (oldCategory.getNumOfDirectProduct() - 1) > 0 ? (oldCategory.getNumOfDirectProduct() - 1) : 0);
            categoryRepo.save(oldCategory);
            newCategory.setNumOfDirectProduct(newCategory.getNumOfDirectProduct() + 1);
            categoryRepo.save(newCategory);
        }
        Product productSaved = save(product);

        //if  :
        if (flags.endsWith("1")) {
            List<Image> images = imageService.getImagesByProduct(productSaved.getId())
                    .stream()
                    .sorted(Comparator.comparingLong(Image::getId))
                    .collect(Collectors.toList());
            Image mainImg = null;
            List<Image> extraImgs = new ArrayList<>();

            for (Image imagei : images) {
                if (!imagei.isMain()) {
                    extraImgs.add(imagei);
                } else {
                    mainImg = imagei;
                }
            }
            if (mainImg.getColor() != mainColor) {
                mainImg.setColor(mainColor);
                imageService.save(mainImg);
            }
            for (int j = 0; j < extraImgs.size(); j++) {
                if (extraImgs.get(j).getColor() != extraColors.get(j)) {
                    Image imgi = extraImgs.get(j);
                    String colori = extraColors.get(j);
                    imgi.setColor(colori);
                    imageService.save(imgi);
                }
            }
        }
        return productSaved;
    }

    @Override
    public Product save(Product product) {
        //when use model attribute if dont have a fill -> that fill will be null when update
        if (product.getId() != null) {
            //update
//            product.setAtbs(getProduct(product.getId()).getAtbs());

            product.setCode(TextUtil.generateCode(product.getName(), product.getId()));
            //rare case: change category
            Category oldCategory = categoryRepo.getCategoryByProductId(product.getId()).
                    orElseThrow(() -> new ResourceNotFoundException("Category of product not found"));
            if (oldCategory.getId() != product.getCategory().getId()) {

                oldCategory.setNumOfDirectProduct((oldCategory.getNumOfDirectProduct() - 1) > 0
                        ? (oldCategory.getNumOfDirectProduct() - 1)
                        : 0);
                categoryRepo.save(oldCategory);
                Category newCategory = product.getCategory();
                newCategory.setNumOfDirectProduct(newCategory.getNumOfDirectProduct() + 1);
                categoryRepo.save(newCategory);
            }

            return productRepo.save(product);
        } else {
            //rare case: add new
            Product productSaved = productRepo.save(product);
            Category category = productSaved.getCategory();
            category.setNumOfDirectProduct(category.getNumOfDirectProduct() + 1);
            categoryRepo.save(category);
            productSaved.setCode(TextUtil.generateCode(product.getName(), product.getId()));
            return productRepo.save(productSaved);
        }
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


    //
    public List<ProductResponseDto> getProductDtoByCategory(Integer categoryId) {
        return productRepo.getProductDtoByCategory(categoryId);
    }

    @Override
    public List<ProductResponseDto> getProductDtosByCategory(Category category) {
        List<ProductResponseDto> rs = new ArrayList<>();
        if (category.getNumOfDirectSubCat() == 0) {
            // no sub category can get only all product of it
            rs = getProductDtoByCategory(category.getId());

        } else if (category.getId() == 1) {
            // root category -> find all product
            rs = getProductDtos();

        } else {
            List<Category> allChildHasProduct = new ArrayList<>();
            Queue<Category> queue = new ArrayDeque<>();
            queue.add(category);
            Category cat;
            while (!queue.isEmpty()) {
                cat = queue.remove();
                if (cat.getNumOfDirectSubCat() != 0) {
                    queue.addAll(categoryRepo.getSubCategoriesByParentId(cat.getId()));
                }
                if (cat.getNumOfDirectProduct() > 0)
                    allChildHasProduct.add(cat);
            }
            for (Category cateHasProduct : allChildHasProduct) {
                rs.addAll(getProductDtoByCategory(cateHasProduct.getId()));
            }

        }
        return rs;
    }
    //

    @Override
    public Product getProductWithImagesAndCategory(Long id) {
        return productRepo.getProductWithImagesAndCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " is not found"));
    }

    @Override
    public List<ProductResponseDto> getProductDtos() {
        return productRepo.getProductDtos();
    }


    public List<Integer> getListIdToQuery(Integer parentId) {
        Category category = categoryRepo.findById(parentId).
                orElseThrow(() -> new ResourceNotFoundException("Category with id " + parentId + " not found"));
        List<Integer> allChildHasProduct = new ArrayList<>();
        Queue<Category> queue = new ArrayDeque<>();
        queue.add(category);
        Category cat;
        while (!queue.isEmpty()) {
            cat = queue.remove();
            if (cat.getNumOfDirectSubCat() != 0) {
                queue.addAll(categoryRepo.getSubCategoriesByParentId(cat.getId()));
            }
            if (cat.getNumOfDirectProduct() > 0)
                allChildHasProduct.add(cat.getId());
        }
        return allChildHasProduct;

    }


}
