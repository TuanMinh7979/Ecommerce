package com.tmt.tmdt.service;

import com.tmt.tmdt.dto.response.CategoryResponseDto;
import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CategoryService {


    Category getCategory(Integer id);

    List<Category> getCategories();

    Page getCategories(Pageable p);


    void deleteById(Integer id);

    void deleteCategories(Integer[] ids);

    boolean existByName(String name);

    List<String> getCategoryNamesByKw(String kw);

    Category getCategoryByName(String name);


    Page<Category> getCategoriesByNameLike(String name, Pageable pageable);


    List<Category> getCategoriesInHierarchicalFromRoot();

    Category add(Category category);

    Category update(Category category);


    Category savePersistence(Category category);

    List<CategoryResponseDto> getCategoryResponseDtos();

    Category getParentByChildId(Integer childId);




    List<CategoryResponseDto> getAvailableForMenuCategory(Integer id);


}
