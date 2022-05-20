package com.tmt.tmdt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tmt.tmdt.converter.ListToStringConverter;
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
import java.util.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)

public class Category extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 2)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private Set<Product> products;

    @JsonIgnore
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "atbs")
    private String atbs;

    // auto-generate from name
    private String code;

    private int numOfDirectProduct;

    private int numOfDirectSubCat;

    //    @JsonIgnore
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "filter")
    private String filter;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    // one to one one one to many
    @JsonIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Category> children = new HashSet<>();


    @Column(name = "childrenIdsInView")
    private String childrenIdsInView;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rootfilter_id")
    private RootFilterEntity rootFilterEntity;


    public Category(String name) {
        this.name = name;
    }

    public Category(String name, Category parent) {
        this.parent = parent;
        this.name = name;
    }

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Category(Integer id, String name, String code, Category parent) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.parent = parent;

    }

}
