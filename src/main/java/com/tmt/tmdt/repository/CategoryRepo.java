package com.tmt.tmdt.repository;

import com.tmt.tmdt.dto.response.CategoryResponseDto;
import com.tmt.tmdt.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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



    @Query(value = "select par.id as id, par.name as name , par.code as code , count(child.id) as numOfDirectSubCat , par.create_at, par.update_at, par.atbs, par.num_of_direct_product ,par.parent_id  from categories par left outer join  categories child on child.parent_id = par.id group by par.id"
            , nativeQuery = true)
    List<Category> getCategoriesWithNumDirectSubCat();


}
