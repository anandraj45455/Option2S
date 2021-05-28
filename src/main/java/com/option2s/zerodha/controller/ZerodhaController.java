package com.option2s.zerodha.controller;

import com.option2s.zerodha.service.ZerodhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/zerodha")
public class ZerodhaController {

    @Autowired
    private ZerodhaService zerodhaService;

    @GetMapping("/kite/connect")
    public String connectKite() {
        zerodhaService.connectKite();

        return "";

    }

}
