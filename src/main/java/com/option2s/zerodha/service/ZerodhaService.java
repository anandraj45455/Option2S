package com.option2s.zerodha.service;

import com.option2s.common.utils.AppHelper;
import com.option2s.zerodha.model.ZerodhaKiteToken;
import com.option2s.zerodha.repository.ZerodhaKiteTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ZerodhaService {

    @Autowired
    private ZerodhaKiteTokenRepository zerodhaKiteTokenRepository;

    public ZerodhaKiteToken save(ZerodhaKiteToken zerodhaKiteToken) {
        return zerodhaKiteTokenRepository.save(zerodhaKiteToken);
    }

    public ZerodhaKiteToken getLatestRequestToken() {
        ZerodhaKiteToken zerodhaKiteToken = zerodhaKiteTokenRepository.findLatestRequestToken();
        return zerodhaKiteToken;
    }

    public ZerodhaKiteToken findTokensByCurrentDate() {
        List<ZerodhaKiteToken> tokens = zerodhaKiteTokenRepository.findTokensByCurrentDate();
        ZerodhaKiteToken zerodhaKiteToken = null;

        for (ZerodhaKiteToken zerodhaKiteToken1 : tokens) {
            if (AppHelper.isPresent(zerodhaKiteToken1.getAccessTokenValue()) &&
                AppHelper.isPresent(zerodhaKiteToken1.getPublicTokenValue())) {
                zerodhaKiteToken = zerodhaKiteToken1;
                break;
            }
        }
        return zerodhaKiteToken;
    }

}
