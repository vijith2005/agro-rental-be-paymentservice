package com.agroconnect.payment.dto;

public record RazorpayOrderResponse(
        String id,
        Integer amount,
        String currency,
        String receipt,
        String status
) {
}
