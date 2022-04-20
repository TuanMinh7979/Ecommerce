package com.tmt.tmdt.repository;

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

    @Query("from Category c  where c.id = :id")
    Optional<Category> getSimpleCategory(@Param("id") Integer id);
//
//    @Query("from Category c join fetch c.children")
//    List<Category> getCategoriesWithChilds();


}
