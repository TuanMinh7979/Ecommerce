package com.tmt.tmdt.entities.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Transient;

@Getter
@Setter

@NoArgsConstructor

public class CatWithNofDirectSubCat {
    private Integer id;
    private String name;
    private String code;
    private Integer parent_id;

    private Integer numOfDirectSubCat;

    public CatWithNofDirectSubCat(Integer id, String name, String code, Integer parent_id, Integer numOfDirectSubCat) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.parent_id = parent_id;
        this.numOfDirectSubCat = numOfDirectSubCat;
    }
}
