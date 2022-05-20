package com.tmt.tmdt.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "filters")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RootFilterEntity {

    //use for common filter option for many category

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    //each key associated with atb key.
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "filter")
    private String filter;

    @OneToMany(mappedBy = "rootFilterEntity")
    private Set<Category> categories;

}
