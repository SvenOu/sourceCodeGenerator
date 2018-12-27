package com.sven.security.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

public final class NoOpPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }


    public static PasswordEncoder getInstance() {
        return INSTANCE;
    }

    private static final PasswordEncoder INSTANCE = new NoOpPasswordEncoder();

    private NoOpPasswordEncoder() { }

}