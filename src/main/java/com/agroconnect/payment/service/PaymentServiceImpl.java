package com.agroconnect.payment.service;

import com.agroconnect.payment.dto.PaymentCreateRequest;
import com.agroconnect.payment.dto.PaymentResponse;
import com.agroconnect.payment.dto.PaymentStatusRequest;
import com.agroconnect.payment.entity.Payment;
import com.agroconnect.payment.entity.PaymentStatus;
import com.agroconnect.payment.exception.BadRequestException;
import com.agroconnect.payment.exception.ResourceConflictException;
import com.agroconnect.payment.exception.ResourceNotFoundException;
import com.agroconnect.payment.repository.PaymentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponse createPayment(Authentication authentication, PaymentCreateRequest request) {
        String userEmail = currentUserEmail(authentication);
        if (!Objects.equals(userEmail, normalize(request.farmerId())) && !currentUserIsAdmin(authentication)) {
            throw new AccessDeniedException("You can only create your own payment");
        }

        Payment payment = new Payment();
        payment.setId("pay-" + UUID.randomUUID());
        payment.setRentalId(trim(request.rentalId()));
        payment.setEquipmentId(trim(request.equipmentId()));
        payment.setEquipmentName(trim(request.equipmentName()));
        payment.setFarmerId(normalize(request.farmerId()));
        payment.setFarmerName(trim(request.farmerName()));
        payment.setOwnerId(normalize(request.ownerId()));
        payment.setOwnerName(trim(request.ownerName()));
        payment.setAmount(request.amount());
        payment.setCurrency("INR");
        payment.setPaymentMethod(trim(request.paymentMethod()));
        payment.setGateway(trimToNull(request.gateway()));
        payment.setTransactionId(defaultIfBlank(request.transactionId(), "txn-" + UUID.randomUUID()));
        payment.setReceiptNumber(defaultIfBlank(request.receiptNumber(), "rcpt-" + UUID.randomUUID()));
        payment.setNote(trimToNull(request.note()));
        payment.setStatus(request.status() == null ? PaymentStatus.PAID : request.status());
        payment.setInitiatedAt(LocalDateTime.now());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setPaidAt(request.status() == null || request.status() == PaymentStatus.PAID ? LocalDateTime.now() : null);

        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    @Override
    public PaymentResponse updateStatus(String id, Authentication authentication, PaymentStatusRequest request) {
        Payment payment = findByIdOrThrow(id);
        assertCanManage(authentication, payment);

        payment.setStatus(request.status());
        payment.setNote(appendNote(payment.getNote(), trimToNull(request.note())));
        payment.setUpdatedAt(LocalDateTime.now());
        if (request.status() == PaymentStatus.PAID && payment.getPaidAt() == null) {
            payment.setPaidAt(LocalDateTime.now());
        }
        return toResponse(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponse getPaymentById(String id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    public List<PaymentResponse> listByFarmer(String farmerId, Authentication authentication) {
        assertRequestedIdentity(authentication, farmerId, "farmer");
        return paymentRepository.findByFarmerIdIgnoreCaseOrderByUpdatedAtDesc(normalize(farmerId))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> listByOwner(String ownerId, Authentication authentication) {
        assertRequestedIdentity(authentication, ownerId, "owner");
        return paymentRepository.findByOwnerIdIgnoreCaseOrderByUpdatedAtDesc(normalize(ownerId))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> listByRental(String rentalId, Authentication authentication) {
        if (!currentUserIsAdmin(authentication)) {
            currentUserEmail(authentication);
        }
        return paymentRepository.findByRentalIdIgnoreCaseOrderByUpdatedAtDesc(normalize(rentalId))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Payment findByIdOrThrow(String id) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("Payment id is required");
        }
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    private void assertCanManage(Authentication authentication, Payment payment) {
        if (currentUserIsAdmin(authentication)) {
            return;
        }
        String email = currentUserEmail(authentication);
        if (!Objects.equals(email, payment.getFarmerId()) && !Objects.equals(email, payment.getOwnerId())) {
            throw new AccessDeniedException("You can only manage your own payment");
        }
    }

    private void assertRequestedIdentity(Authentication authentication, String pathIdentity, String label) {
        if (currentUserIsAdmin(authentication)) {
            return;
        }

        String email = currentUserEmail(authentication);
        if (!Objects.equals(email, normalize(pathIdentity))) {
            throw new AccessDeniedException("You can only view your own " + label + " payments");
        }
    }

    private boolean currentUserIsAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority));
    }

    private String currentUserEmail(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new AccessDeniedException("Authentication required");
        }
        return normalize(authentication.getName());
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getRentalId(),
                payment.getEquipmentId(),
                payment.getEquipmentName(),
                payment.getFarmerId(),
                payment.getFarmerName(),
                payment.getOwnerId(),
                payment.getOwnerName(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getPaymentMethod(),
                payment.getGateway(),
                payment.getTransactionId(),
                payment.getReceiptNumber(),
                payment.getNote(),
                payment.getStatus(),
                payment.getInitiatedAt(),
                payment.getPaidAt(),
                payment.getUpdatedAt(),
                payment.getCreatedAt()
        );
    }

    private String appendNote(String existing, String next) {
        if (next == null || next.isBlank()) {
            return existing;
        }
        if (existing == null || existing.isBlank()) {
            return next;
        }
        return existing + " | " + next;
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().toLowerCase(Locale.ROOT);
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String defaultIfBlank(String value, String fallback) {
        String trimmed = trimToNull(value);
        return trimmed == null ? fallback : trimmed;
    }
}
