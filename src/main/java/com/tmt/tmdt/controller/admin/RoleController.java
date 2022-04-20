package com.tmt.tmdt.controller.admin;

import com.tmt.tmdt.dto.response.ViewResponseApi;
import com.tmt.tmdt.entities.Category;
import com.tmt.tmdt.entities.Role;
import com.tmt.tmdt.service.PermissionService;
import com.tmt.tmdt.service.RoleService;
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
import java.util.List;

@Controller
@RequestMapping("/admin/role")
@RequiredArgsConstructor
public class RoleController {
    private final PermissionService permissionService;
    private final RoleService roleService;


    @GetMapping("api/viewApi")
    @ResponseBody
    public ViewResponseApi<List<Category>> getRole(Model model,
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
        Page rolePage = null;
        if (searchNameTerm != null && !searchNameTerm.isEmpty()) {

            rolePage = roleService.getRolesByNameLike(searchNameTerm, pageable);
        } else {
            rolePage = roleService.getRoles(pageable);
        }
        List data = rolePage.getContent();
        int totalPage = rolePage.getTotalPages();

        return new ViewResponseApi<>(totalPage, data);
    }

    @GetMapping("")
    public String index() {
        return "admin/role/index";
    }

    @GetMapping("add")
    public String showAddForm(Model model) {
        model.addAttribute("role", new Role());
        model.addAttribute("parentPermissions", permissionService.getPermissionByParent(1));
        return "admin/role/add";
    }

    @PostMapping("save")
    public String save(Model model, @Valid @ModelAttribute("role") Role role, BindingResult result) {

        if (roleService.existByName(role.getName())) {
            result.rejectValue("name", "nameIsExist");
        }
        if (!result.hasErrors()) {
            roleService.save(role);
            return "redirect:/admin/role/";
        }
        model.addAttribute("parentPermissions", permissionService.getPermissionByParent(1));
        return "admin/role/add";


    }

    @GetMapping("edit/{idx}")
    //rest api : showUpdateForm , showAddForm => getCategory(get)(just for update)
    public String showUpdateForm(Model model, @PathVariable("idx") String idx) {
        Role role = null;
        model.addAttribute("parentPermissions", permissionService.getPermissionByParent(1));
        try {
            //Catch casting exception
            role = roleService.getRoleWithPermissions(Integer.parseInt(idx));
        } catch (NumberFormatException e) {
            role = roleService.getRoleByNameWithPermissions(idx);
        }
        model.addAttribute("role", role);

        return "admin/role/edit";

    }

    @PostMapping("update")
    public String update(Model model, @Valid @ModelAttribute("role") Role role, BindingResult result) {
        //client just send name what is active("if no checkbox checked no param named permission been send")
        if (roleService.existByName(role.getName())) {
            result.rejectValue("name", "nameIsExist");
        }
        if (!result.hasErrors()) {
            roleService.save(role);
            return "redirect:/admin/role/";
        }

        model.addAttribute("parentPermissions", permissionService.getPermissionByParent(1));

        return "admin/role/edit";


    }

    @PostMapping("api/delete/{id}")
    @ResponseBody
    //call with ajax
    public ResponseEntity<Integer> deleteRole(@PathVariable Integer id) {
        roleService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }


    @PostMapping("api/delete")
    @ResponseBody
    public ResponseEntity<Integer[]> deleteCategories(@RequestBody Integer[] ids) {
        roleService.deleteRoles(ids);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }


    @GetMapping("/api/{id}/active-permission-ids")
    @ResponseBody
    public List<Integer> getPermission(@PathVariable("id") Integer roleId) {

        return permissionService.getPermissionIdsByRoleId(roleId);
    }




}
