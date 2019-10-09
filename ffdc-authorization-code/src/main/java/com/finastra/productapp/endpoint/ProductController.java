package com.finastra.productapp.endpoint;

import com.finastra.productapp.api.FfdcApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class ProductController {

    @Autowired
    private FfdcApi ffdcApi;

    @Value("${auth.strong}")
    private boolean strongAuth;

    @RequestMapping("/results")
    public String resultsPage (Model model){
        model.addAttribute("entities" ,ffdcApi.getReferenceSourcesLegalEntities().getItems());
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