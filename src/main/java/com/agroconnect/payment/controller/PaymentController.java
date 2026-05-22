package com.agroconnect.payment.controller;

import com.agroconnect.payment.dto.PaymentCreateRequest;
import com.agroconnect.payment.dto.PaymentResponse;
import com.agroconnect.payment.dto.RazorpayOrderRequest;
import com.agroconnect.payment.dto.RazorpayOrderResponse;
import com.agroconnect.payment.dto.PaymentStatusRequest;
import com.agroconnect.payment.service.PaymentService;
import com.agroconnect.payment.service.RazorpayOrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final RazorpayOrderService razorpayOrderService;

    public PaymentController(PaymentService paymentService, RazorpayOrderService razorpayOrderService) {
        this.paymentService = paymentService;
        this.razorpayOrderService = razorpayOrderService;
    }

    @PostMapping({"/razorpay/orders", "/razorpay/orders/"})
    @PreAuthorize("hasAnyRole('FARMER', 'ADMIN')")
    public ResponseEntity<RazorpayOrderResponse> createRazorpayOrder(
            @Valid @RequestBody RazorpayOrderRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(razorpayOrderService.createOrder(request));
    }

    @PostMapping({"", "/"})
    @PreAuthorize("hasAnyRole('FARMER', 'ADMIN')")
    public ResponseEntity<PaymentResponse> create(
            Authentication authentication,
            @Valid @RequestBody PaymentCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(authentication, request));
    }

    @PutMapping({"/{id}/status", "/{id}/status/"})
    @PreAuthorize("hasAnyRole('FARMER', 'OWNER', 'ADMIN')")
    public ResponseEntity<PaymentResponse> updateStatus(
            @PathVariable String id,
            Authentication authentication,
            @Valid @RequestBody PaymentStatusRequest request
    ) {
        return ResponseEntity.ok(paymentService.updateStatus(id, authentication, request));
    }

    @GetMapping({"/{id}", "/{id}/"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping({"/farmer/{farmerId}", "/farmer/{farmerId}/"})
    @PreAuthorize("hasAnyRole('FARMER', 'ADMIN')")
    public ResponseEntity<List<PaymentResponse>> byFarmer(
            @PathVariable String farmerId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(paymentService.listByFarmer(farmerId, authentication));
    }

    @GetMapping({"/owner/{ownerId}", "/owner/{ownerId}/"})
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<List<PaymentResponse>> byOwner(
            @PathVariable String ownerId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(paymentService.listByOwner(ownerId, authentication));
    }

    @GetMapping({"/rental/{rentalId}", "/rental/{rentalId}/"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PaymentResponse>> byRental(
            @PathVariable String rentalId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(paymentService.listByRental(rentalId, authentication));
    }
}
