package com.option2s.zerodha.controller;

import com.option2s.zerodha.model.ZerodhaKiteToken;
import com.option2s.zerodha.repository.ZerodhaKiteTokenRepository;
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
    private ZerodhaKiteTokenRepository zerodhaKiteTokenRepository;

    @GetMapping("/settings")
    public String main(Model model) {
        return "settings";
    }

    @GetMapping("{tab}")
    public String tab(Model model, @PathVariable String tab) {

        if (tab.equalsIgnoreCase("zerodha_kite_token")) {
            List<ZerodhaKiteToken> zerodhaKiteTokens = zerodhaKiteTokenRepository.findAll();
            model.addAttribute("zerodhaKiteTokens", zerodhaKiteTokens);
            model.addAttribute("zerodhaKiteToken", new ZerodhaKiteToken());

            return "_zerodha_kite_token";
        } else if (tab.equalsIgnoreCase("tab2")) {

            return "_tab2";
        } else if (tab.equalsIgnoreCase("tab3")) {

            return "_tab3";
        } else {
            return "empty";
        }

    }

    @PostMapping("/save_token")
    public RedirectView saveRefreshToken(ZerodhaKiteToken zerodhaKiteToken) {
        zerodhaKiteToken.setDate(new Date());
        zerodhaKiteTokenRepository.save(zerodhaKiteToken);
        return new RedirectView("/settings");
    }


}
