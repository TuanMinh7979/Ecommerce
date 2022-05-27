package com.tmt.tmdt.controller.admin;

import com.tmt.tmdt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller

@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

    private final CategoryService categoryService;


    @GetMapping("")
    public String index() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
            username = ((UserDetails) principal).getUsername();
//            List<SimpleGrantedAuthority> authorityList = (List<SimpleGrantedAuthority>) ((UserDetails) principal).getAuthorities();
            ArrayList<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>(((UserDetails) principal).getAuthorities());
            for (GrantedAuthority sa : authorityList) {
                System.out.print(sa.getAuthority()+"---");
            }
        } else {
            username = principal.toString();
            System.out.println("BBBBBBBBBBBBBBBBBBBBBBBB");
            List<SimpleGrantedAuthority> authorityList = (List<SimpleGrantedAuthority>) ((UserDetails) principal).getAuthorities();
            for (SimpleGrantedAuthority sa : authorityList) {
                System.out.print(sa.getAuthority()+":");
            }
        }


        return "admin/admin";

    }


}
