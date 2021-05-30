package com.option2s.zerodha.service;

import com.option2s.common.utils.AppHelper;
import com.option2s.zerodha.model.ZerodhaKiteToken;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
                AppHelper.isPresent(kiteConnect.getAccessToken()) &&
                AppHelper.isPresent(kiteConnect.getPublicToken())) {
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
                    kiteConnect = null;

                }
            });

            ZerodhaKiteToken zerodhaKiteToken = zerodhaService.findTokensByCurrentDate();

            if (zerodhaKiteToken != null && zerodhaKiteToken.getAccessTokenValue() != null &&
                    zerodhaKiteToken.getPublicTokenValue() != null) {
                kiteConnect.setAccessToken(zerodhaKiteToken.getAccessTokenValue());
                kiteConnect.setPublicToken(zerodhaKiteToken.getPublicTokenValue());
                kiteConnectStatus = "KiteConnect Token exists: " + zerodhaKiteToken.getPublicTokenValue();
            } else {
                kiteConnectStatus = "KiteConnect token doesn't exists. Generating new tokens";
                System.out.println(kiteConnectStatus);

                zerodhaKiteToken = zerodhaService.getLatestRequestToken();

                if (zerodhaKiteToken != null) {
                    User user =  kiteConnect.generateSession(zerodhaKiteToken.getRequestTokenValue(), kiteApiSecret);
                    String newAccessToken = user.accessToken;
                    String newPublicToken = user.publicToken;
                    kiteConnect.setAccessToken(newAccessToken);
                    kiteConnect.setPublicToken(newPublicToken);

                    zerodhaKiteToken.setAccessTokenValue(newAccessToken);
                    zerodhaKiteToken.setPublicTokenValue(newPublicToken);
                    zerodhaService.save(zerodhaKiteToken);
                    kiteConnectStatus = "KiteConnect token generation is success: " + user.publicToken;
                } else {
                    kiteConnectStatus = "KiteConnect request token is null. " +
                            "Trigger this url " + loginUrl + " to generate request token";
                    kiteConnect = null;
                }
            }
            System.out.println(kiteConnectStatus);

        } catch (KiteException | Exception e) {
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
