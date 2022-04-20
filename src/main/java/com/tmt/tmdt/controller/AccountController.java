package com.tmt.tmdt.controller;

import com.tmt.tmdt.constant.UserStatus;
import com.tmt.tmdt.entities.UserEntity;
import com.tmt.tmdt.service.RoleService;
import com.tmt.tmdt.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Set;

@Controller
@RequiredArgsConstructor

public class AccountController {


    private final UserEntityService userEntityService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm() {
        return "home/account/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userEntity", new UserEntity());

        return "home/account/register";
    }

    @PostMapping("/register")
    public String createNewCustomer(Model model, @Valid @ModelAttribute UserEntity customer, BindingResult result) {

        if (userEntityService.existByUsername(customer.getUsername())) {
            result.rejectValue("username", "nameIsExist");
        }
        if (customer.getPassword().isEmpty() || !customer.getPassword().equals(customer.getConfirmPassword())) {

            result.rejectValue("confirmPassword", "error.userentity", "Confirm password does not match");
        }
        if (!result.hasErrors()) {
            customer.setStatus(UserStatus.ENABLE);
            customer.getRoles().add(roleService.getRoleByName("ROLE_CUSTOMER"));
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            userEntityService.save(customer);
            return "redirect:/";
        }
        return "home/account/register";
    }


    @GetMapping("api/user")
    @ResponseBody
    public UserEntity getUser(@AuthenticationPrincipal UserDetails user) {
        return userEntityService.getUserByUsername(user.getUsername());
    }


}


