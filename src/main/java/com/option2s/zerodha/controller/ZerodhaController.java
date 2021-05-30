package com.option2s.zerodha.controller;

import com.option2s.zerodha.model.RefreshToken;
import com.option2s.zerodha.repository.RefreshTokenRepository;
import com.option2s.zerodha.service.ZerodhaApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
import java.util.List;

@Controller
public class ZerodhaController {

    @Autowired
    private ZerodhaApiService zerodhaApiService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @GetMapping("/settings")
    public String main(Model model) {
        return "settings";
    }

    @GetMapping("{tab}")
    public String tab(Model model, @PathVariable String tab) {

        if (tab.equalsIgnoreCase("refresh_token")) {
            List<RefreshToken> refreshTokens = refreshTokenRepository.findAll();
            model.addAttribute("refreshTokens", refreshTokens);
            model.addAttribute("refreshToken", new RefreshToken());

            return "_refresh_token";
        } else if (tab.equalsIgnoreCase("tab2")) {

            return "_tab2";
        } else if (tab.equalsIgnoreCase("tab3")) {

            return "_tab3";
        } else {
            return "empty";
        }

    }

    @PostMapping("/save_refresh_token")
    public RedirectView saveRefreshToken(RefreshToken refreshToken) {
        refreshToken.setDate(new Date());
        refreshTokenRepository.save(refreshToken);
        return new RedirectView("/settings");
    }


}
