package com.option2s.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderHelper {

    public static void main(String[] args) throws Exception {
        String rawPassword = "test1234";
        String encodedPassword = encodePassword(rawPassword);
        System.out.println(encodedPassword);

    }

    public static String encodePassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

}
