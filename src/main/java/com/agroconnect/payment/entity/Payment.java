package com.agroconnect.payment.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    private String id;

    private String rentalId;
    private String equipmentId;
    private String equipmentName;

    private String farmerId;
    private String farmerName;

    private String ownerId;
    private String ownerName;

    private Integer amount;
    private String currency;
    private String paymentMethod;
    private String gateway;
    private String transactionId;
    private String receiptNumber;
    private String note;

    private PaymentStatus status;
    private LocalDateTime initiatedAt;
    private LocalDateTime paidAt;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    @Version
    private Long version;
}
