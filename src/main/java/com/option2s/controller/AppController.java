package com.option2s.controller;

import com.option2s.model.User;
import com.option2s.repository.UserRepository;
import com.option2s.utils.PasswordEncoderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AppController {

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        String password = user.getPassword();
        String encodedPassword = PasswordEncoderHelper.encodePassword(password);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "registration_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("users", userList);

        return "users";
    }



}
