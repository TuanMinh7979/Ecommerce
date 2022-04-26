package com.tmt.tmdt.service.impl;

import com.tmt.tmdt.dto.response.CategoryResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.pojo.CatWithNofDirectSubCat;
import com.tmt.tmdt.exception.ResourceNotFoundException;
import com.tmt.tmdt.repository.CategoryRepo;
import com.tmt.tmdt.service.CategoryService;
import com.tmt.tmdt.service.ProductService;
import com.tmt.tmdt.util.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    //dont have input but away depend on a input field -> save method
    private final CategoryRepo cateRepository;


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
//        category.setAttributes(category.getParent().getAttributes());
        Category categorySaved = cateRepository.save(category);
        categorySaved.setCode(TextUtil.generateCode(categorySaved.getName(), Long.valueOf(categorySaved.getId())));
        return save(categorySaved);
    }

    @Override
    public Category update(Category category) {
        category.setCode(TextUtil.generateCode(category.getName(), Long.valueOf(category.getId())));
        //set old category attribute
//        category.setAttributes(getCategory(category.getId()).getAttributes());
        return save(category);
    }

    @Override
    public Category save(Category category) {
        //use for update
        return cateRepository.save(category);
    }

    @Override
    public List<CategoryResponseDto> getCategoryResponseDtos() {
        return cateRepository.getCategoryResponseDtos();
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


    //category in hierarchical
    @Override
    public List<Category> getCategoriesInHierarchicalFromRoot() {
        List<Category> categoriesRs = new ArrayList<>();
        List<Category> categoryList = cateRepository.findAll();
        reRender(categoriesRs, categoryList, 1, "");
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


    //non overide
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

    @PersistenceContext
    private EntityManager em;
    @Transactional
    public List<CatWithNofDirectSubCat> getCategoriesWithNofDirectSubCat() {
        List<CatWithNofDirectSubCat> cats = (List<CatWithNofDirectSubCat>) em.createNativeQuery
                ("select par.id , par.name , par.code ,par.parent_id , count(child.id) as numOfDirectSubCat from categories par left outer join  categories child on child.parent_id = par.id group by par.id", "catForForm").getResultList();
        return cats;
    }


}
