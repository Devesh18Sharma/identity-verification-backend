package com.dev.identity_verification_backend.dto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

// Lombok's @Data generates getters, setters, toString, equals, hashCode automatically
@Data
public class VerificationRequestDto {
    // Field names MUST match the names used in the frontend FormData
    private MultipartFile documentFront;
    private MultipartFile documentBack; // Optional
    private MultipartFile selfie;
}