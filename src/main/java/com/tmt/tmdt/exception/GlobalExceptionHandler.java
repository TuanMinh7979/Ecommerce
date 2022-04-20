package com.tmt.tmdt.exception;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public String handleResourceNotFound(Exception e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "admin/error/404Err";
    }






}


