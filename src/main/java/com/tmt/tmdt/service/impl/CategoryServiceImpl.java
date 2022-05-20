package com.tmt.tmdt.service.impl;

import com.tmt.tmdt.dto.response.CategoryResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.exception.ResourceNotFoundException;
import com.tmt.tmdt.repository.CategoryRepo;
import com.tmt.tmdt.repository.ProductRepo;
import com.tmt.tmdt.service.CategoryService;
import com.tmt.tmdt.util.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    // dont have input but away depend on a input field -> save method
    private final CategoryRepo cateRepository;
    private final ProductRepo productRepo;

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
        // category.setAttributes(category.getParent().getAttributes());

        Category parentCategory = category.getParent();

        category.setAtbs(parentCategory.getAtbs());

        category.setFilter(parentCategory.getFilter());

        category.setRootFilterEntity(parentCategory.getRootFilterEntity());

        category.setChildrenIdsInView("");

        parentCategory.setNumOfDirectSubCat(parentCategory.getNumOfDirectSubCat() + 1);
        cateRepository.save(parentCategory);

        Category savedCategory = cateRepository.save(category);
        savedCategory.setCode(TextUtil.generateCode(savedCategory.getName(), Long.valueOf(savedCategory.getId())));

        return cateRepository.save(category);
    }


    @Override
    public Category update(Category category) {
//        entityManager.merge()
        Category oldCategory = getCategory(category.getId());
        if (category.getChildrenIdsInView() == null) {
            category.setChildrenIdsInView(oldCategory.getChildrenIdsInView());
        }
        category.setCode(TextUtil.generateCode(category.getName(), Long.valueOf(category.getId())));
        category.setRootFilterEntity(oldCategory.getRootFilterEntity());
        if (category.getId() == 1) return cateRepository.save(category);

        if (oldCategory.getParent().getId() != category.getParent().getId()) {
//            change parent category//
            Category oldParentCat = getCategory(category.getParent().getId());

            Category newParentCat = category.getParent();

            newParentCat.setNumOfDirectSubCat(newParentCat.getNumOfDirectSubCat() + 1);
            cateRepository.save(newParentCat);


            oldParentCat.setNumOfDirectSubCat((oldParentCat.getNumOfDirectSubCat() - 1) > 0 ? (oldParentCat.getNumOfDirectSubCat() - 1) : 0);
            cateRepository.save(oldParentCat);
        }
        return cateRepository.save(category);
    }


    @Override
    public Category savePersistence(Category persistenceCate) {
        //    persistenceCate that is from server , not from client
        return cateRepository.save(persistenceCate);
    }


    @Override
    public List<CategoryResponseDto> getCategoryResponseDtos() {
        return cateRepository.getCategoryResponseDtos();
    }


    @Override
    public Category getParentByChildId(Integer childId) {
        return cateRepository.getParentByChildId(childId);
    }

    @Override
    public void deleteById(Integer id) {

        Category parent = getParentByChildId(id);
        parent.setNumOfDirectSubCat((parent.getNumOfDirectSubCat() - 1) > 0 ? (parent.getNumOfDirectSubCat() - 1) : 0);

        if (parent.getChildrenIdsInView().indexOf(String.valueOf(id)) != -1) {
            String idChildrenInView = parent.getChildrenIdsInView();
            idChildrenInView.replace(String.valueOf(id), "");
            idChildrenInView = idChildrenInView.trim();
            parent.setChildrenIdsInView(idChildrenInView);
        }
        cateRepository.save(parent);
        cateRepository.deleteById(id);
    }

    @Override
    public void deleteCategories(Integer[] ids) {
        for (Integer id : ids) {
            deleteById(id);
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

        return cateRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category with name " + name + "not found"));

    }

    @Override
    public Page<Category> getCategoriesByNameLike(String name, Pageable pageable) {
        return cateRepository.getCategoriesByNameLike(name, pageable);
    }

    // category in hierarchical
    @Override
    public List<Category> getCategoriesInHierarchicalFromRoot() {
        List<Category> categoriesRs = new ArrayList<>();

        reRender(categoriesRs, cateRepository.findAll(), 1, "");
        return categoriesRs;

    }


    // non overide
    public void reRender(List<Category> rs, List<Category> all, Integer id, String split) {
        for (Category category : all) {
            if (category.getParent() != null && category.getParent().getId() == id) {
                String name = split + category.getName();
                String code = category.getCode();
                rs.add(new Category(category.getId(), name, code));
                if (category.getNumOfDirectSubCat() > 0)
                    reRender(rs, all, category.getId(), split.concat("--"));
            }

        }

    }

    public List<CategoryResponseDto> getAvailableForMenuCategory(Integer parentId) {
        Category category = cateRepository.findById(parentId).
                orElseThrow(() -> new ResourceNotFoundException("Category with id " + parentId + " not found"));
        List<CategoryResponseDto> allChildHasProduct = new ArrayList<>();
        Queue<Category> queue = new ArrayDeque<>();
        queue.add(category);
        Category cat;
        while (!queue.isEmpty()) {
            cat = queue.remove();
            if (cat.getNumOfDirectSubCat() != 0) {
                queue.addAll(cateRepository.getSubCategoriesByParentId(cat.getId()));
            }
            if (cat.getNumOfDirectProduct() > 0)
                allChildHasProduct.add(new CategoryResponseDto(cat.getId(), cat.getName(), cat.getNumOfDirectProduct()));
        }
        return allChildHasProduct;

    }


}
