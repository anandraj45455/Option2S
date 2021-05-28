package com.option2s.zerodha.service;

import com.zerodhatech.kiteconnect.KiteConnect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZerodhaService {

    @Value("${spring.kite.api-key}")
    private String kiteApiKey;

    @Value("${spring.kite.api-secret}")
    private String kiteApiSecret;

    @Value("${spring.kite.user-id}")
    private String kiteUserId;

    public static void main(String[] args) throws Exception {
        new ZerodhaService().connectKite();
    }

    public void connectKite() {
        KiteConnect kiteConnect = new KiteConnect(kiteApiKey);
        kiteConnect.setUserId(kiteUserId);
        String loginUrl = kiteConnect.getLoginURL();
        System.out.println(loginUrl);

    }

}
