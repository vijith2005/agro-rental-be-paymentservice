package com.agroconnect.payment.dto;

import com.agroconnect.payment.entity.PaymentStatus;
import java.time.LocalDateTime;

public record PaymentResponse(
        String id,
        String rentalId,
        String equipmentId,
        String equipmentName,
        String farmerId,
        String farmerName,
        String ownerId,
        String ownerName,
        Integer amount,
        String currency,
        String paymentMethod,
        String gateway,
        String transactionId,
        String receiptNumber,
        String note,
        PaymentStatus status,
        LocalDateTime initiatedAt,
        LocalDateTime paidAt,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {
}
