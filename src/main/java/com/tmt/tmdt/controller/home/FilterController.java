package com.tmt.tmdt.controller.home;

import com.tmt.tmdt.entities.pojo.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("filter")
@Controller
public class FilterController {

    private final Filter filter;


    //filter for product category page
    @PostMapping("/ui-opt-name")
    @ResponseBody
    public Map<String, String> getUiOptNameMap(@RequestBody List<String> filterAtbs) {
        Map<String, String> ori = filter.getUiOptName();


        Map<String, String> kvAtbUiname = new HashMap<>();
        for (String atbName : filterAtbs) {
            kvAtbUiname.put(atbName, ori.get(atbName));
        }

        return kvAtbUiname;
    }

}
