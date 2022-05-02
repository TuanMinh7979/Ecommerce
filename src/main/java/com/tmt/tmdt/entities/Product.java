package com.tmt.tmdt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")

public class Product extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;

    private BigDecimal price;

    private String mainImageLink = defaultImage();


    private String shortDescription;


    private String fullDescription;

//    private boolean enable;

    private Float discountPercent;


    @JsonIgnore
    @OneToMany(mappedBy = "product", orphanRemoval = true)
    //delete image when delete product
    private Set<Image> images = new HashSet<>();


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String code;


    @Transient
    public String defaultImage() {
        return "/resource/img/default.png";
    }


    @JsonIgnore
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "atbs")
    private String atbs;

    public Product(Long id, String name, BigDecimal price, String mainImageLink, String code) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.mainImageLink = mainImageLink;
        this.code = code;
    }
}
