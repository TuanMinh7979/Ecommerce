package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Product;
import com.tmt.tmdt.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("select name from Product where name like :kw% ")
    List<String> getNamesByKw(@Param("kw") String kw);

    @Query("from Product where name like :name% ")
    List<Product> getProductsByName(@Param("name") String name);

    Optional<Product> getProductByName(String name);

    @Query(value = "select * from products where name like ?1%",
            countQuery = "select count(id) from products where name like ?1%",
            nativeQuery = true)
    Page<Product> getProductsByName(String name, Pageable pageable);

    @Query(value = "select * from products where category_id = ?1 "
            , countQuery = "select count(id) from products where category_id = ?1"
            , nativeQuery = true)
    Page<Product> getProductsByCategory(Long categoryId, Pageable pageable);

    boolean existsByName(String name);

    @Query(value = "select * from products where category_id = ?1 and name like ?2%"
            , countQuery = "select count(*) from products where category_id = ?1 and name like ?2%"
            , nativeQuery = true)
    Page<Product> getProductsByCategoryAndNameLike(Long categoryId, String name, Pageable pageable);

    @Query("select p from Product p left join fetch p.images  where p.id= :id")
    Product getProductWithImages(@Param("id") Long id);

    @Query(value = "from Product  where category.id = :categoryId")
    List<Product> getProductsByCategory(@Param("categoryId") Integer id);
}
