package com.tmt.tmdt.entities.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@NoArgsConstructor
public class Attribute implements Serializable {
    @JsonProperty("value")
    private String[]  value;

    @JsonProperty("active")
    private int active;

    @JsonProperty("filter")
    private int filter;

    @JsonProperty("filterValue")
    private Map<String, String> filterValue = new HashMap<>();

    public Attribute(String[] value, int active, int filter, Map<String, String> filterValue) {
        this.value = value;
        this.active = active;
        this.filter = filter;
        this.filterValue = filterValue;
    }

    public Attribute(String[] value, int active, int filter) {
        this.value = value;
        this.active = active;
        this.filter = filter;
    }
}
