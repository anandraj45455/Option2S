package com.option2s.zerodha.service;

import com.option2s.common.utils.AppHelper;
import com.option2s.zerodha.model.ZerodhaKiteToken;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        System.out.println("kiteApiKey: " + kiteApiKey);
        System.out.println("kiteUserId: " + kiteUserId);

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
                    generateAccessToken();
                } else {
                    kiteConnectStatus = "KiteConnect request token is null. " +
                            "Trigger this url " + loginUrl + " to generate request token";
                    kiteConnect = null;
                }
            }
            System.out.println(kiteConnectStatus);

        } catch (Exception e) {
            kiteConnect = null;
            kiteConnectStatus = "ERROR: " + kiteConnectStatus + ": " + e.getMessage();
            e.printStackTrace();
        }

        return kiteConnect;
    }

    public void generateAccessToken() {
        try {
            ZerodhaKiteToken zerodhaKiteToken = zerodhaService.getLatestRequestToken();
            User user =  kiteConnect.generateSession(zerodhaKiteToken.getRequestTokenValue(), kiteApiSecret);
            String newAccessToken = user.accessToken;
            String newPublicToken = user.publicToken;
            kiteConnect.setAccessToken(newAccessToken);
            kiteConnect.setPublicToken(newPublicToken);

            zerodhaKiteToken.setAccessTokenValue(newAccessToken);
            zerodhaKiteToken.setPublicTokenValue(newPublicToken);
            zerodhaService.save(zerodhaKiteToken);
            kiteConnectStatus = "KiteConnect token generation is success: " + user.publicToken;
        } catch (KiteException | Exception e) {
            e.printStackTrace();
        }
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


    // ################# API METHODS ############## //

    public void test() {
        getProfile();
        //getInstruments();
        getQuote();
    }

    public void getProfile() {
        try {
            Profile profile = kiteConnect().getProfile();
            System.out.println(profile.userName);
        } catch (KiteException | Exception e) {
            e.printStackTrace();
        }
    }

    public void getQuote() {

        String[] instruments = {"NSE:NIFTY 50", "NSE:NIFTY BANK"};
        try {
            Map<String, Quote> quotes = kiteConnect().getQuote(instruments);
            OHLC niftyOHLC = quotes.get("NSE:NIFTY 50").ohlc;
            System.out.println(niftyOHLC.open);
            System.out.println(niftyOHLC.high);
            System.out.println(niftyOHLC.low);
            System.out.println(niftyOHLC.close);

            OHLC bankNiftyOHLC = quotes.get("NSE:NIFTY BANK").ohlc;
            System.out.println(bankNiftyOHLC.open);
            System.out.println(bankNiftyOHLC.high);
            System.out.println(bankNiftyOHLC.low);
            System.out.println(bankNiftyOHLC.close);

        } catch (KiteException | Exception e) {
            e.printStackTrace();
        }
    }

    public void getInstruments() {
        try {
            List<Instrument> instruments = kiteConnect().getInstruments();

            System.out.println("Total instruments size: " + instruments.size());

            for (Instrument instrument : instruments) {
                if (instrument.getTradingsymbol().contains("BANKNIFTY")) {
                    System.out.println("*****************************");
                    System.out.println(instrument.getName() + " => " + instrument.getTradingsymbol());
                }
            }

        } catch (KiteException | Exception e) {
            e.printStackTrace();
        }
    }


}
