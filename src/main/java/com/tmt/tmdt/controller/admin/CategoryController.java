package com.tmt.tmdt.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmt.tmdt.dto.response.ViewResponseApi;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.pojo.Attribute;
import com.tmt.tmdt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/category")
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("")
    public String index() {
        return "admin/category/index";
    }

    @GetMapping("api/viewApi")
    @ResponseBody
    public ViewResponseApi<List<Category>> getCategories(Model model,
                                                         @RequestParam(name = "page", required = false) String pageParam,
                                                         @RequestParam(name = "limit", required = false) String limitParam,
                                                         @RequestParam(name = "sortBy", required = false) String sortBy,
                                                         @RequestParam(name = "sortDirection", required = false) String sortDirection,
                                                         @RequestParam(name = "searchNameTerm", required = false) String searchNameTerm) {


        String sortField = sortBy == null ? "id" : sortBy;
        Sort sort = (sortDirection == null || sortDirection.equals("asc")) ? Sort.by(Sort.Direction.ASC, sortField)
                : Sort.by(Sort.Direction.DESC, sortField);
        int page = pageParam == null ? 0 : Integer.parseInt(pageParam) - 1;
        int limit = limitParam == null ? 5 : Integer.parseInt(limitParam);

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page categoryPage = null;
        if (searchNameTerm != null && !searchNameTerm.isEmpty()) {

            categoryPage = categoryService.getCategoriesByNameLike(searchNameTerm, pageable);
        } else {
            categoryPage = categoryService.getCategories(pageable);
        }
        List data = categoryPage.getContent();
        int totalPage = categoryPage.getTotalPages();

        return new ViewResponseApi<>(totalPage, data);
    }

    //ADD
    @GetMapping("add")
    public String showAddForm(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "admin/category/add";
    }


    @PostMapping("add")
    //rest api save => add(post), edit(put)
    public String add(Model model, @Valid @ModelAttribute("category") Category category, BindingResult result) {
        if (categoryService.existByName(category.getName())) {
            result.rejectValue("name", "nameIsExist");
        }

        if (!result.hasErrors()) {
            categoryService.add(category);
            return "redirect:/admin/category";
        }


        return "admin/category/add";
    }
    //-ADD

    //UPDATE
    @GetMapping("update/{idx}")
    //rest api : showUpdateForm , showAddForm => getCategory(get)(just for update)
    public String showUpdateForm(Model model, @PathVariable("idx") String idx) {
        Category category = null;
        try {
            //Catch casting exception
            Integer id = Integer.parseInt(idx);
            category = categoryService.getCategory(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            category = categoryService.getCategoryByName(idx);
        }
        model.addAttribute("category", category);
        return "admin/category/edit";

    }

    @PostMapping("update")
    //rest api save => add(post), update(put)
    //FOR UPDATE: update in rest api must a id in path. but in mvc dont need it
    public String update(Model model, @Valid @ModelAttribute("category") Category category, BindingResult result) {

        if (!result.hasErrors()) {
            categoryService.update(category);
            return "redirect:/admin/category";
        }

        return "admin/category/edit";
    }
    //-UPDATE


    //DELETE
    @PostMapping("api/delete/{id}")
    //call by ajax
    public ResponseEntity<Integer> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }


    @PostMapping("api/delete")
    public ResponseEntity<Integer[]> deleteCategories(@RequestBody Integer[] ids) {
        categoryService.deleteCategories(ids);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }
    //-DELETE


    //FOR ATTRIBUTE

    @GetMapping("api/{id}/attributes")
    @ResponseBody
    public String getAttributesByCategoryId(@PathVariable("id") Integer id) {
        return categoryService.getCategory(id).getAtbs();
    }

    @PostMapping("api/{id}/attributes/update")
    @ResponseBody
    public Category updateAttributes(@PathVariable("id") Integer id, @RequestBody String newAttributes) {
        Category category = categoryService.getCategory(id);
        category.setAtbs(newAttributes);
        return categoryService.save(category);

    }

    @PostMapping("api/{id}/attributes/update/filterset-forallchild")
    @ResponseBody
    public Category updateAttributesWithFilterSetForChild(@PathVariable("id") Integer id, @RequestBody String newAttributes) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Attribute> mapAtb = objectMapper.readValue(newAttributes, Map.class);

        Category category = categoryService.getCategory(id);
        category.setAtbs(newAttributes);
        Category savedCat = categoryService.save(category);


        List<Category> allSubCat = new ArrayList<>();
        Queue<Category> queue = new ArrayDeque<>();
        queue.add(savedCat);
        Category cat;
        while (!queue.isEmpty()) {
            cat = queue.remove();
            if (cat.getNumOfDirectSubCat() != 0) {
                //this is direct subcat
                queue.addAll(categoryService.getSubCategoriesByParentId(cat.getId()));

            }
            allSubCat.add(cat);
        }


        for (int i = 1; i < allSubCat.size(); i++) {
            Category subCati = allSubCat.get(i);
            Map<String, Attribute> atbs = objectMapper.readValue(subCati.getAtbs(), Map.class);
            for (String atbiName : atbs.keySet()) {
            }
            for (String keyi : atbs.keySet()) {

                System.out.println("::::::::::::::::::::::::::");
                System.out.println(keyi);
//                mapAtbi.get(keyi).getValue();
                if (atbs.get(keyi).getFilter() == 1 && atbs.get(keyi).getFilterValue() != null) {
                    atbs.get(keyi).setFilterValue(mapAtb.get(keyi).getFilterValue());
                }
            }

//            String newUpdateAttribute = objectMapper.writeValueAsString(atbs.getAttributeMap());
//            subCati.setAtbs(newUpdateAttribute);
            categoryService.save(subCati);
        }


        return savedCat;

    }


    //-FOR ATTRIBUTE


}
