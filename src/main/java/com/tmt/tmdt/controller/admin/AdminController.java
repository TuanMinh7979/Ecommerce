package com.tmt.tmdt.controller.admin;

import com.tmt.tmdt.repository.OrderRepo;
import com.tmt.tmdt.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {

    private final CategoryService categoryService;
    private final OrderRepo orderRepo;

    @GetMapping("")
    public String index() {
        logUser();

        return "admin/admin";

    }

    public void logUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {

            username = ((UserDetails) principal).getUsername();
            log.warn("LOGGED TO ADMIN BY :" + username);
            ArrayList<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>(((UserDetails) principal).getAuthorities());
            for (GrantedAuthority sa : authorityList) {
                System.out.print(sa.getAuthority() + "---");
            }
        }
    }

    //call by ajax
    @GetMapping("stat/product-stat")
    @ResponseBody
    public List<Object[]> getProductTurnoverDStat(@RequestParam(value = "fromDate", required = false) String fromDatePar,
                                                  @RequestParam(value = "toDate", required = false) String toDatePar) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromDate = null;
        LocalDate toDate = null;
        System.out.println("VAO DAYU  ________________________________");

        if (fromDatePar == null && toDatePar == null) {
            return orderRepo.getProductTurnoverDStat();
        } else if (fromDatePar != null) {
            fromDate = LocalDate.parse(fromDatePar, formatter);
            return orderRepo.getProductTurnoverDStatFromDate(fromDate);
        } else if (toDatePar != null) {
            toDate = LocalDate.parse(toDatePar, formatter);
            return orderRepo.getProductTurnoverDStatToDate(toDate);
        } else {
            fromDate = LocalDate.parse(fromDatePar, formatter);
            toDate = LocalDate.parse(toDatePar, formatter);
            return orderRepo.getProductTurnoverDStatBetween(fromDate, toDate);
        }
    }


}
