package com.tmt.tmdt.repository;

import com.tmt.tmdt.dto.response.ProductResponseDto;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("select name from Product where name like :kw% ")
    List<String> getNamesByKw(@Param("kw") String kw);

    // @Query("from Product where name like :name% ")
    // List<Product> getProductsByName(@Param("name") String name);

    Optional<Product> getProductByName(String name);

    @Query(value = "select new com.tmt.tmdt.dto.response.ProductResponseDto(p.name, p.price, p.discountPercent, p.mainImageLink, p.code) from Product as p where p.name like :name%")
    List<ProductResponseDto> getProductResDtosByNameLike(@Param("name") String name);

    @Query(value = "select * from products where name like ?1%", countQuery = "select count(id) from products where name like ?1%", nativeQuery = true)
    Page<Product> getProductsByName(String name, Pageable pageable);

    @Query(value = "select * from products where category_id = ?1 ", countQuery = "select count(id) from products where category_id = ?1", nativeQuery = true)
    Page<Product> getProductsByCategory(Long categoryId, Pageable pageable);

    boolean existsByName(String name);

    @Query(value = "select * from products where category_id = ?1 and name like ?2%", countQuery = "select count(*) from products where category_id = ?1 and name like ?2%", nativeQuery = true)
    Page<Product> getProductsByCategoryAndNameLike(Long categoryId, String name, Pageable pageable);

    @Query("select p from Product p left join fetch p.images where p.id= :id")
        // dont need join fetch with many to one relation ship if just use cateogyId(in
        // the table product)
        // data bidding by default we ca have id of category through product.category.id
        // at thymleaf
    Optional<Product> getProductWithImages(@Param("id") Long id);

    @Query("select p from Product p left join fetch p.images join fetch p.category where p.id = :id")
    Optional<Product> getProductWithImagesAndCategory(@Param("id") Long id);

    // for home


    @Query(value = "select * from products where category_id = ?1", nativeQuery = true)
    List<Product> getProductsByCategory(Integer id);

    @Query(value = "select count(p.id) from Product p where p.category.id= :id")
    int countProductByCategory(@Param("id") Integer categoryId);

    @Query(value = "select new com.tmt.tmdt.dto.response.ProductResponseDto(p.name, p.price, p.discountPercent, p.mainImageLink, p.code) from Product as p")
    List<ProductResponseDto> getProductDtos();

    @Query(value = "select p.code from Product p where p.name= :name")
    String getCodeByName(@Param("name") String name);
}
