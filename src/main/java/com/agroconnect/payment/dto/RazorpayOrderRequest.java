package com.agroconnect.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RazorpayOrderRequest(
        @NotNull @Positive Integer amount,
        @NotBlank String receipt,
        String description
) {
}
