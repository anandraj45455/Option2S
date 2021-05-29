package com.option2s.zerodha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BankNiftyController {

    @GetMapping("/bank-nifty")
    public String getBankNifty(Model model) {
        model.addAttribute("pageTitle", "Bank Nifty");
        return "bank_nifty";
    }

}
