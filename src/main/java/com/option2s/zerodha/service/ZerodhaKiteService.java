package com.option2s.zerodha.service;

import com.option2s.zerodha.model.ZerodhaKiteToken;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
public class ZerodhaKiteService {

    @Value("${spring.kite.api-key}")
    private String kiteApiKey;

    @Value("${spring.kite.api-secret}")
    private String kiteApiSecret;

    @Value("${spring.kite.user-id}")
    private String kiteUserId;

    @Autowired
    private ZerodhaService zerodhaService;

    public KiteConnect kiteConnect = null;

    public KiteConnect kiteConnect() {

        try {
            kiteConnect = new KiteConnect(kiteApiKey, true);
            kiteConnect.setUserId(kiteUserId);

            String loginUrl = kiteConnect.getLoginURL();
            System.out.println(loginUrl);

            kiteConnect.setSessionExpiryHook(new SessionExpiryHook() {
                @Override
                public void sessionExpired() {
                    System.out.println("session expired");
                }
            });

            ZerodhaKiteToken zerodhaKiteToken = zerodhaService.findTokensByDate(new Date());

            if (zerodhaKiteToken.getAccessTokenValue() != null && zerodhaKiteToken.getPublicTokenValue() != null) {
                System.out.println("Token exists");
                kiteConnect.setAccessToken(zerodhaKiteToken.getAccessTokenValue());
                kiteConnect.setPublicToken(zerodhaKiteToken.getPublicTokenValue());

            } else {
                System.out.println("Token doesn't exists. Generating tokens");
                User user =  kiteConnect.generateSession(zerodhaService.getLatestRequestToken(), kiteApiSecret);
                kiteConnect.setAccessToken(user.accessToken);
                kiteConnect.setPublicToken(user.publicToken);
            }

        } catch (KiteException e) {
            kiteConnect = null;
            e.printStackTrace();
        } catch (IOException e) {
            kiteConnect = null;
            e.printStackTrace();
        }

        return kiteConnect;
    }

    public String getKiteLoginUrl() {
        kiteConnect = new KiteConnect(kiteApiKey, true);
        kiteConnect.setUserId(kiteUserId);
        return kiteConnect.getLoginURL();
    }

    public User generateSession() {

        User user = null;

        try {
            user =  kiteConnect.generateSession(zerodhaService.getLatestRequestToken(), kiteApiSecret);
            System.out.println("UUUUUUUUUUUUUUUUUUUUUUUU");
            kiteConnect.setAccessToken(user.accessToken);
            kiteConnect.setPublicToken(user.publicToken);
        } catch (KiteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;

    }








}
