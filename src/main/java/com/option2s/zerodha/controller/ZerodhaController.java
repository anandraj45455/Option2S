package com.option2s.zerodha.controller;

import com.option2s.zerodha.service.ZerodhaApiService;
import com.option2s.zerodha.service.ZerodhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;

@Controller
public class ZerodhaController {

    @Autowired
    private ZerodhaApiService zerodhaApiService;

    @GetMapping("/settings")
    public String main(Model model) {
        return "settings";
    }

    @GetMapping("{tab}")
    public String tab(@PathVariable String tab) {
        if (Arrays.asList("refresh_token", "tab2", "tab3")
                .contains(tab)) {
            return "_" + tab;
        }

        return "empty";
    }


}
