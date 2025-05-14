package com.dev.identity_verification_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // Needed if using @Data with other constructors
@AllArgsConstructor // Creates a constructor with all fields
public class VerificationResponseDto {
    private String verificationId;
}