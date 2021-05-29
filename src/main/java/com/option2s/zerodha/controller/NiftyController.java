package com.option2s.zerodha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NiftyController {

    @GetMapping("/nifty")
    public String getNifty(Model model) {
        model.addAttribute("pageTitle", "Nifty");
        return "nifty";
    }

}
