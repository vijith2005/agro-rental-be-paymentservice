package com.agroconnect.payment.repository;

import com.agroconnect.payment.entity.Payment;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByFarmerIdIgnoreCaseOrderByUpdatedAtDesc(String farmerId);
    List<Payment> findByOwnerIdIgnoreCaseOrderByUpdatedAtDesc(String ownerId);
    List<Payment> findByRentalIdIgnoreCaseOrderByUpdatedAtDesc(String rentalId);
}
