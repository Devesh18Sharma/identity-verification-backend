package com.dev.identity_verification_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponseDto {
    // Using the same enum/type definition helps consistency, but a String is simpler here for now
    private String status; // e.g., "PENDING", "APPROVED", "FAILED" etc. matches frontend type VerificationStatusValue
}