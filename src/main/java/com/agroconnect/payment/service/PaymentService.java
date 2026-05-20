package com.agroconnect.payment.service;

import com.agroconnect.payment.dto.PaymentCreateRequest;
import com.agroconnect.payment.dto.PaymentResponse;
import com.agroconnect.payment.dto.PaymentStatusRequest;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface PaymentService {
    PaymentResponse createPayment(Authentication authentication, PaymentCreateRequest request);
    PaymentResponse updateStatus(String id, Authentication authentication, PaymentStatusRequest request);
    PaymentResponse getPaymentById(String id);
    List<PaymentResponse> listByFarmer(String farmerId, Authentication authentication);
    List<PaymentResponse> listByOwner(String ownerId, Authentication authentication);
    List<PaymentResponse> listByRental(String rentalId, Authentication authentication);
}
