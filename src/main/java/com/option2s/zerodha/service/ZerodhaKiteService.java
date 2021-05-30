package com.option2s.zerodha.service;

import com.option2s.zerodha.model.ZerodhaKiteToken;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import org.json.JSONObject;
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

    public String kiteConnectStatus = "";

    public KiteConnect kiteConnect() {

        if (kiteConnect != null &&
                !kiteConnect.getAccessToken().isEmpty() && !kiteConnect.getPublicToken().isEmpty()) {
            kiteConnectStatus = "KiteConnect Token exists: " + kiteConnect.getPublicToken();
            System.out.println(kiteConnectStatus);

            return kiteConnect;

        }

        System.out.println("KiteConnect is null. Initializing KiteConnect");

        try {
            kiteConnect = new KiteConnect(kiteApiKey, true);
            kiteConnect.setUserId(kiteUserId);

            String loginUrl = kiteConnect.getLoginURL();
            System.out.println(loginUrl);

            kiteConnect.setSessionExpiryHook(new SessionExpiryHook() {
                @Override
                public void sessionExpired() {
                    kiteConnectStatus = "KiteConnect Token is invalid or has expired. " +
                            "Trigger this url " + loginUrl + " to generate request token";
                    System.out.println(kiteConnectStatus);

                }
            });

            ZerodhaKiteToken zerodhaKiteToken = zerodhaService.findTokensByDate(new Date());

            if (zerodhaKiteToken.getAccessTokenValue() != null && zerodhaKiteToken.getPublicTokenValue() != null) {
                kiteConnect.setAccessToken(zerodhaKiteToken.getAccessTokenValue());
                kiteConnect.setPublicToken(zerodhaKiteToken.getPublicTokenValue());
                kiteConnectStatus = "KiteConnect Token exists: " + zerodhaKiteToken.getPublicTokenValue();
            } else {
                kiteConnectStatus = "KiteConnect token doesn't exists. Generating new tokens";
                System.out.println(kiteConnectStatus);
                User user =  kiteConnect.generateSession(zerodhaService.getLatestRequestToken(), kiteApiSecret);
                kiteConnect.setAccessToken(user.accessToken);
                kiteConnect.setPublicToken(user.publicToken);

                zerodhaKiteToken.setAccessTokenValue(user.accessToken);
                zerodhaKiteToken.setPublicTokenValue(user.publicToken);
                zerodhaService.save(zerodhaKiteToken);
                kiteConnectStatus = "KiteConnect token generation is success: " + user.publicToken;
            }
            System.out.println(kiteConnectStatus);

        } catch (KiteException e) {
            kiteConnect = null;
            kiteConnectStatus = "ERROR: " + kiteConnectStatus + ": " + e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            kiteConnect = null;
            kiteConnectStatus = "ERROR: " + kiteConnectStatus + ": " + e.getMessage();
            e.printStackTrace();
        }

        return kiteConnect;
    }

    public String getKiteConnectStatus() {
        return kiteConnectStatus;
    }

    public JSONObject logout() {
        JSONObject response = new JSONObject();
        try {
            response = kiteConnect.logout();
            kiteConnect = null;
            kiteConnectStatus = "";
        } catch (KiteException | Exception e) {
            e.printStackTrace();
            response.put("ErrorItem", "KiteConnectLogout");
            response.put("errorMsg", e.getMessage());
        }
        return response;
    }


}
