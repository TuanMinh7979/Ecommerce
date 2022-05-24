package com.tmt.tmdt.repository;

import com.tmt.tmdt.dto.response.CategoryResponseDto;
import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);

    //source for auto complete
    @Query("select name from Category where name like :name% ")
    List<String> getCategoryNamesByKw(@Param("name") String name);

    //find by name for autocomplete
    Optional<Category> findByName(String name);

    @Query(value = "SELECT * FROM categories WHERE name like ?1%",
            countQuery = "SELECT count(*) FROM categories WHERE name like ?1%",
            nativeQuery = true)
    Page<Category> getCategoriesByNameLike(String name, Pageable pageable);

    @Query(value = "select new com.tmt.tmdt.dto.response.CategoryResponseDto(cat.id,cat.name, cat.code, cat.parent.id) from Category as cat")
    List<CategoryResponseDto> getCategoryResponseDtos();

//    @Query(value = "select count(child.id) as numOfDirectSubCat from categories par left outer join  categories child on child.parent_id = par.id where par.id = ?1 group by par.id", nativeQuery = true)
//    int getNofSubCatByCategoryId(Integer categoryId);

    @Query(value = "from Category c where c.parent.id = :catId")
    List<Category> getSubCategoriesByParentId(@Param("catId") Integer parentId);

    @Query(value = "select * from categories par where par.id in (select child.parent_id from categories child where child.id= ?1)", nativeQuery = true)
    Category getParentByChildId(Integer id);

//    @Query(value = "select new com.tmt.tmdt.dto.response.ProductResponseDto(p.name, p.price, p.discountPercent, p.mainImageLink, p.code) from Product as p")
//    List<ProductResponseDto> getProductDtos();
}
