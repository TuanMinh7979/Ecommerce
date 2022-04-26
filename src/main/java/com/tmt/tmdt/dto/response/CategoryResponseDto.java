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
@AllArgsConstructor
@NoArgsConstructor

public class CategoryResponseDto {
    private int id;
    private String name;
    private String code;
    private Integer parentId;
    private int numOfDirectSubCat;

    public CategoryResponseDto(int id, String name, String code, Integer parentId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.parentId = parentId;
    }



}
