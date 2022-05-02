package com.tmt.tmdt.controller.home;

import com.tmt.tmdt.entities.pojo.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("product-filter")
@RestController
public class ProductFilterApi {

    private final Filter filter;


    @PostMapping("list-map")
    public Map<String, Map<String, String>> getListMapByIds(@RequestBody String[] ids) {
        //khong the dung request param cho array dc(chi co so va string co the )
        Map<String, Map<String, String>> rs = new HashMap<>();

        for (String idi : ids) {
            switch (idi) {
                case "Ram":
                    rs.put("Ram", filter.getRamMap());
                    break;
                case "Storage":
                    rs.put("Storage", filter.getStorageMap());
                    break;
                default:
                    break;

            }
        }
        return rs;
    }


}
