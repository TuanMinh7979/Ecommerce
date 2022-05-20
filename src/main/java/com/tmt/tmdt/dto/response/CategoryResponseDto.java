package com.tmt.tmdt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor

public class CategoryResponseDto {
    private Integer id;
    private String name;
    private String code;
    private Integer parentId;
    private Integer numOfDirectSubCat;
    private Integer numOfDirectProduct;


    public CategoryResponseDto(int id, String name, String code, Integer parentId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.parentId = parentId;
    }


    public CategoryResponseDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryResponseDto(int id, String name, int numOfDirectProduct) {
        this.id = id;
        this.name = name;
        this.numOfDirectProduct = numOfDirectProduct;
    }


}
