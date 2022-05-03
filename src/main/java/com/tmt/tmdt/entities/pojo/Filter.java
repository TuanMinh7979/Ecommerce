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

public class Filter {

    //filterOptionUIName
    @Value("#{${uiOptName}}")
    Map<String, String> uiOptName;

    //root phone category
    @Value("#{${filter.phone.ram}}")
    Map<String, String> phoneRam;

    @Value("#{${filter.phone.ramMap}}")
    Map<String, String> phoneRamMap;

    @Value("#{${filter.phone.storage}}")
    Map<String, String> phoneStorage;


    @Value("#{${filter.phone.storageMap}}")
    Map<String, String> phoneStorageMap;


    //root laptop category
    @Value("#{${filter.laptop.ram}}")
    Map<String, String> laptopRam;

    @Value("#{${filter.laptop.ramMap}}")
    Map<String, String> laptopRamMap;

    @Value("#{${filter.laptop.hardDisk}}")
    Map<String, String> laptopHardDisk;


    @Value("#{${filter.laptop.hardDiskMap}}")
    Map<String, String> laptopHardDiskMap;


}
