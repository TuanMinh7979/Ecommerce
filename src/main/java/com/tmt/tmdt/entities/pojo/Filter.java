package com.tmt.tmdt.entities.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@PropertySource(value = "classpath:filterAndSort.properties")
@Getter
@Setter

public class Filter {


    @Value("#{${filter.ram}}")
    Map<String, String> ram;

    @Value("#{${filter.ramMap}}")
    Map<String, String> ramMap;

    @Value("#{${filter.price}}")
    Map<String, String> price;

    @Value("#{${filter.priceMap}}")
    Map<String, String> priceMap;

    @Value("#{${filter.storageMap}}")
    Map<String, String> storageMap;


}
