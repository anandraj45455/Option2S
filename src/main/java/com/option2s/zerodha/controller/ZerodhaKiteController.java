package com.option2s.zerodha.controller;

import com.option2s.zerodha.service.ZerodhaKiteService;
import com.zerodhatech.kiteconnect.KiteConnect;
import org.json.JSONObject;
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
        String kiteConnectStatus = zerodhaKiteService.getKiteConnectStatus();
        JSONObject result = new JSONObject();
        if (kiteConnect != null) {
            result.put("status", "Success");
            result.put("kiteConnectStatus", kiteConnectStatus);
        } else {
            result.put("status", "Failed");
            result.put("kiteConnectStatus", kiteConnectStatus);
        }

        return result.toString();
    }

    @GetMapping("/kite/logout")
    public String logoutKiteConnect() {
        JSONObject response = zerodhaKiteService.logout();
        return response.toString();
    }

}
