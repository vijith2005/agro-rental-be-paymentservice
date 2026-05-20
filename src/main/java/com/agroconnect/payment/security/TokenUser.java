package com.agroconnect.payment.security;

public record TokenUser(
        Long userId,
        String email,
        String role
) {
}
