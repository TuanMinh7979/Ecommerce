package com.tmt.tmdt.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Attribute implements Serializable {

    private String id;
    private String name;
    private List<Object> value;
    private int active;

    private int filter;

    public Attribute(String id, String name, List<Object> value, int active) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.active = active;
    }
}
