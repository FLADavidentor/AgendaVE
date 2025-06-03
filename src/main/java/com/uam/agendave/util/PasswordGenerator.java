package com.uam.agendave.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        String rawPassword = "123";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println(encodedPassword);
    }
}
