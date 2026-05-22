package com.agroconnect.payment.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
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

    public Payment() {
    }

    public Payment(
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
            LocalDateTime createdAt,
            Long version
    ) {
        this.id = id;
        this.rentalId = rentalId;
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.farmerId = farmerId;
        this.farmerName = farmerName;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.gateway = gateway;
        this.transactionId = transactionId;
        this.receiptNumber = receiptNumber;
        this.note = note;
        this.status = status;
        this.initiatedAt = initiatedAt;
        this.paidAt = paidAt;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getInitiatedAt() {
        return initiatedAt;
    }

    public void setInitiatedAt(LocalDateTime initiatedAt) {
        this.initiatedAt = initiatedAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
