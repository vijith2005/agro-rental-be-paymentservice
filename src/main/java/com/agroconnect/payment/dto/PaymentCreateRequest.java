package com.agroconnect.payment.dto;

import com.agroconnect.payment.entity.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentCreateRequest(
        @NotBlank String rentalId,
        @NotBlank String equipmentId,
        @NotBlank String equipmentName,
        @NotBlank String farmerId,
        @NotBlank String farmerName,
        @NotBlank String ownerId,
        @NotBlank String ownerName,
        @NotNull @Positive Integer amount,
        @NotBlank String paymentMethod,
        String gateway,
        String transactionId,
        String receiptNumber,
        String note,
        PaymentStatus status
) {
}
