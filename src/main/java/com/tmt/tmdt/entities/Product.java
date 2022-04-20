package com.tmt.tmdt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
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


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String code;


    @Transient
    public String defaultImage() {
        return "/resource/img/default.png";
    }

    //image is special case bc can not view the old file in update form now so can not use CascadeType.Persist
    //and do not use helper method

    @JsonIgnore
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "attributes")
    private Set<Attribute> attributes = new HashSet<>();

    //Attribute helper method
    public Product addAttribute(Attribute newAttribute) {
        this.attributes.add(newAttribute);
        return this;
    }

    public Product removeAttribute(Attribute toDelAttribute) {
        this.attributes.remove(toDelAttribute);
        return this;
    }
    //-Attribute helper method


}
