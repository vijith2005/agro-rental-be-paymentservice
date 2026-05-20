package com.agroconnect.payment.dto;

import com.agroconnect.payment.entity.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public record PaymentStatusRequest(
        @NotNull PaymentStatus status,
        String note
) {
}
