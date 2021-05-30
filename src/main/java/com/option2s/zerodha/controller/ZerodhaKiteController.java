package com.option2s.zerodha.controller;

import com.option2s.zerodha.service.ZerodhaKiteService;
import com.zerodhatech.kiteconnect.KiteConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/option2s")
public class ZerodhaKiteController {

    @Autowired
    public ZerodhaKiteService zerodhaKiteService;

    @GetMapping("/kite/connect")
    public String connectKite() {
        KiteConnect kiteConnect = zerodhaKiteService.kiteConnect();
        return kiteConnect.toString();
    }



}
