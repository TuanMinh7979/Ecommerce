package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {
    @Query("from Image i where i.product.id = :productId")
    List<Image> getImagesByProduct(@Param("productId") Long id);
}
