package com.bolsa.factura.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@Controller
public class HomeController {
    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "/home")
    public String loadHome(Model model, Locale locale) {

        model.addAttribute("titulo", messageSource.getMessage("home.title", null, locale));
        return "homePage";

    }
}
