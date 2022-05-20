package com.tmt.tmdt.entities.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@PropertySource(value = "classpath:filterAndSort.properties")
@Getter
@Setter
public class FilterQuery {

    @Value("#{${uiOptName}}")
    Map<String, String> uiOptName;

    @Value("#{${filter.query}}")
    Map<String, String> getQueryMap;
}
