package com.agroconnect.payment.service;

import com.agroconnect.payment.config.RazorpayProperties;
import com.agroconnect.payment.dto.RazorpayOrderRequest;
import com.agroconnect.payment.dto.RazorpayOrderResponse;
import com.agroconnect.payment.exception.BadRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RazorpayOrderService {

    private static final URI RAZORPAY_ORDERS_URI = URI.create("https://api.razorpay.com/v1/orders");

    private final RazorpayProperties properties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public RazorpayOrderService(RazorpayProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newHttpClient();
    }

    public RazorpayOrderResponse createOrder(RazorpayOrderRequest request) {
        String keyId = trimToNull(properties.getKeyId());
        String keySecret = trimToNull(properties.getKeySecret());
        if (!StringUtils.hasText(keyId) || !StringUtils.hasText(keySecret)) {
            throw new BadRequestException("Razorpay is not configured on the payment service");
        }

        try {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("amount", request.amount() * 100);
            payload.put("currency", "INR");
            payload.put("receipt", trimToNull(request.receipt()));
            payload.put("payment_capture", 1);
            if (StringUtils.hasText(request.description())) {
                Map<String, String> notes = new LinkedHashMap<>();
                notes.put("description", request.description().trim());
                payload.put("notes", notes);
            }

            String body = objectMapper.writeValueAsString(payload);
            String credentials = Base64.getEncoder().encodeToString((keyId + ":" + keySecret).getBytes(StandardCharsets.UTF_8));

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(RAZORPAY_ORDERS_URI)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic " + credentials)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new BadRequestException(extractErrorMessage(response.body()));
            }

            JsonNode node = objectMapper.readTree(response.body());
            return new RazorpayOrderResponse(
                    node.path("id").asText(),
                    node.path("amount").isNumber() ? node.path("amount").asInt() : request.amount() * 100,
                    node.path("currency").asText("INR"),
                    node.path("receipt").asText(request.receipt()),
                    node.path("status").asText("created")
            );
        } catch (BadRequestException ex) {
            throw ex;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new BadRequestException("Unable to create Razorpay order");
        } catch (Exception ex) {
            throw new BadRequestException("Unable to create Razorpay order");
        }
    }

    private String extractErrorMessage(String body) {
        try {
            JsonNode node = objectMapper.readTree(body);
            String description = node.path("error").path("description").asText();
            if (StringUtils.hasText(description)) {
                return description;
            }
        } catch (Exception ignored) {
        }
        return "Unable to create Razorpay order";
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
