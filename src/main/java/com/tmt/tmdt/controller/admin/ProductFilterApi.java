package com.tmt.tmdt.controller.admin;

import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.pojo.Filter;
import com.tmt.tmdt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("admin/categoryfilter/")
@RestController

public class ProductFilterApi {

    private final Filter filter;
    private final CategoryService catService;

    // common case
    // category is one of a root categories
    private String getFilterRootParentName(Integer id) {
        Category curCat = catService.getCategory(id);
        while (curCat != null) {
            Category tempParCat = catService.getParentByChildId(curCat.getId());
            if (tempParCat != null && tempParCat.getId() == 1)
                return curCat.getName();
            curCat = tempParCat;
        }
        return null;

    }

    // special case
    // category that for fitler is not a root categories
    private String getSpecificFilterParentName(Integer childId, List<String> parentNames) {

        Category parCat = catService.getParentByChildId(childId);
        while (parCat != null) {
            String curParName = parCat.getName();
            if (parentNames.contains(curParName)) {
                return curParName;
            } else {
                parCat = catService.getParentByChildId(parCat.getId());
            }
        }

        return null;

    }

    // for admin page
    @PostMapping("/list-map")
    @ResponseBody
    public Map<String, Map<String, String>> getListMap(@RequestBody Map<String, Object> data) {
        Integer catId = (Integer) data.get("categoryId");
        List<String> atbNames = (List<String>) data.get("atbNames");

        // special case put here if have

        // common case
        String rootParentName = getFilterRootParentName(catId);
        if (rootParentName.equals("Phone")) {
            return filter.getPhoneFilterMaps(atbNames);
        }
        if (rootParentName.equals("Laptop")) {
            return filter.getLaptopFilterMaps(atbNames);
        }

        return null;

    }

    // public Map<String, Map<String, String>> getPhoneFilterMaps(List<String>
    // keyIds) {

    // Map<String, Map<String, String>> rs = new HashMap<>();

    // for (String keyId : keyIds) {
    // switch (keyId) {
    // case "Ram":
    // rs.put("Ram", filter.getPhoneRamMap());
    // break;
    // case "Storage":
    // rs.put("Storage", filter.getPhoneStorageMap());
    // break;
    // default:
    // break;

    // }
    // }
    // return rs;
    // }

    // public Map<String, Map<String, String>> getLaptopFilterMaps(List<String>
    // keyIds) {
    // Map<String, Map<String, String>> rs = new HashMap<>();

    // for (String keyId : keyIds) {
    // switch (keyId) {
    // case "Ram":
    // rs.put("Ram", filter.getLaptopRamMap());
    // break;
    // case "Hard disk":
    // rs.put("Hard disk", filter.getLaptopHardDiskMap());
    // break;
    // default:
    // break;

    // }
    // }
    // return rs;
    // }
}
