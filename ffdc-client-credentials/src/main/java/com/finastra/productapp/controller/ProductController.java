package com.finastra.productapp.controller;

import com.finastra.productapp.api.service.FfdcService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
class ProductController {
    @Value("${auth.strong}")
    private boolean strongAuth;
    private final FfdcService ffdcService;

    @RequestMapping("/results")
    public String resultsPage (Model model){
        model.addAttribute("countries" ,ffdcService.getReferentialDataCountries().getCountries());
        model.addAttribute("strongAuth", strongAuth);
        return "results";
    }

    @RequestMapping("/")
    public String indexPage (Model model){
        model.addAttribute("strongAuth", strongAuth);
        return "index";
    }

    @RequestMapping("/logout")
    public String logout(Model model) {
        model.addAttribute("strongAuth", strongAuth);
        return "logout";
    }

}