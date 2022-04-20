package com.tmt.tmdt.service.impl;

import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.exception.ResourceNotFoundException;
import com.tmt.tmdt.repository.CategoryRepo;
import com.tmt.tmdt.service.CategoryService;
import com.tmt.tmdt.service.ProductService;
import com.tmt.tmdt.util.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    //dont have input but away depend on a input field -> save method
    private final CategoryRepo cateRepository;
    private final ProductService productService;

    @Override
    public Category getCategory(Integer id) {
        return cateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
    }

    @Override
    public List<Category> getCategories() {
        return cateRepository.findAll();
    }

    @Override
    public Page getCategories(Pageable p) {
        return cateRepository.findAll(p);
    }

    @Override
    public Category add(Category category) {
        category.setAttributes(category.getParent().getAttributes());
        Category categorySaved = cateRepository.save(category);
        categorySaved.setCode(TextUtil.generateCode(categorySaved.getName(), Long.valueOf(categorySaved.getId())));
        return cateRepository.save(category);
    }

    @Override
    public Category save(Category category) {
        //use for update
        category.setCode(TextUtil.generateCode(category.getName(), Long.valueOf(category.getId())));
        return cateRepository.save(category);
    }




    @Override
    public void deleteById(Integer id) {

        cateRepository.deleteById(id);
    }

    @Override
    public void deleteCategories(Integer[] ids) {
        for (Integer id : ids) {
            cateRepository.deleteById(id);
        }
    }

    @Override
    public boolean existByName(String name) {
        return cateRepository.existsByName(name);
    }

    @Override
    public List<String> getCategoryNamesByKw(String kw) {
        return cateRepository.getCategoryNamesByKw(kw);
    }

    @Override
    public Category getCategoryByName(String name) {

        return cateRepository.findByName(name).
                orElseThrow(() -> new ResourceNotFoundException("Category with name " + name + "not found"));

    }

    @Override
    public Page<Category> getCategoriesByNameLike(String name, Pageable pageable) {
        return cateRepository.getCategoriesByNameLike(name, pageable);
    }


//    @Override
//    @Transactional
//    public Category addProductToCategory(Integer cateId, Long productId) {
//        Category cate = getCategory(cateId);
//        Product product = productService.getProduct(productId);
//        cate.getProducts().add(product);
//        return cate;
//    }
//
//    @Override
//    @Transactional
//    public Category removeProductFromCategory(Integer cateId, Long productId) {
//        Category cate = getCategory(cateId);
//        Product product = productService.getProduct(productId);
//        cate.getProducts().remove(product);
//        return cate;
//    }

    //category in hierarchical
    @Override
    public List<Category> getCategoriesInHierarchicalFromRoot() {
        List<Category> categoriesRs = new ArrayList<>();
        reRender(categoriesRs, cateRepository.findAll(), 1, "");
        return categoriesRs;

    }

    @Override
    public List<Category> getCategoriesInHierarchicalFromRootWithOut(int i) {
        List<Category> categories = getCategoriesInHierarchicalFromRoot();
        for (Category category : categories) {
            if (category.getId() == i) {
                categories.remove(category);
                break;
            }
        }
        return categories;
    }


    public void reRender(List<Category> rs, List<Category> all, Integer id, String split) {
        for (Category category : all) {
            if (category.getParent() != null && category.getParent().getId() == id) {
                String name = split + category.getName();
                String code = category.getCode();
                rs.add(new Category(category.getId(), name, code));
                if (!category.getChildren().isEmpty()) reRender(rs, all, category.getId(), split.concat("--"));

            }

        }

    }


}
