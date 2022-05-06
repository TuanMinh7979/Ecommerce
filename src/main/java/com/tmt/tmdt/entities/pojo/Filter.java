package com.tmt.tmdt.entities.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@PropertySource(value = "classpath:filterAndSort.properties")
@Getter
@Setter

public class Filter {

    // filterOptionUIName
    @Value("#{${uiOptName}}")
    Map<String, String> uiOptName;

    // FOR CLIENT
    // root phone category

    @Value("#{${filter.phone.ramMap}}")
    Map<String, String> phoneRamMap;

    @Value("#{${filter.phone.storageMap}}")
    Map<String, String> phoneStorageMap;

    @Value("#{${filter.laptop.ramMap}}")
    Map<String, String> laptopRamMap;

    @Value("#{${filter.laptop.hardDiskMap}}")
    Map<String, String> laptopHardDiskMap;

    public Map<String, Map<String, String>> getPhoneFilterMaps(List<String> keyIds) {

        Map<String, Map<String, String>> rs = new HashMap<>();

        for (String keyId : keyIds) {
            switch (keyId) {
                case "Ram":
                    rs.put("Ram", getPhoneRamMap());
                    break;
                case "Storage":
                    rs.put("Storage", getPhoneStorageMap());
                    break;
                default:
                    break;

            }
        }
        return rs;
    }

    public Map<String, Map<String, String>> getLaptopFilterMaps(List<String> keyIds) {
        Map<String, Map<String, String>> rs = new HashMap<>();

        for (String keyId : keyIds) {
            switch (keyId) {
                case "Ram":
                    rs.put("Ram", getLaptopRamMap());
                    break;
                case "Hard disk":
                    rs.put("Hard disk", getLaptopHardDiskMap());
                    break;
                default:
                    break;

            }
        }
        return rs;
    }

    public List<Map<String, String>> getAllMap() {
        List<Map<String, String>> listMap = new ArrayList<>();
        listMap.add(getPhoneRamMap());
        listMap.add(getPhoneStorageMap());
        listMap.add(getLaptopRamMap());
        listMap.add(getLaptopHardDiskMap());
        return listMap;
    }

    @Value("#{${filter.query}}")
    Map<String, String> getQueryMap;

}
