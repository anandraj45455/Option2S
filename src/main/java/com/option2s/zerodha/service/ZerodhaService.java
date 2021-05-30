package com.option2s.zerodha.service;

import com.option2s.zerodha.model.ZerodhaKiteToken;
import com.option2s.zerodha.repository.ZerodhaKiteTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ZerodhaService {

    @Autowired
    private ZerodhaKiteTokenRepository zerodhaKiteTokenRepository;

    public String getLatestRequestToken() {
        ZerodhaKiteToken zerodhaKiteToken = zerodhaKiteTokenRepository.findLatest();
        return zerodhaKiteToken.getRequestTokenValue();
    }

    public ZerodhaKiteToken getZerodhaKiteToken() {
        return zerodhaKiteTokenRepository.findLatest();
    }

    public ZerodhaKiteToken findTokensByDate(Date date) {

        return zerodhaKiteTokenRepository.findLatest();
    }

}
