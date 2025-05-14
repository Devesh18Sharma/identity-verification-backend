package com.dev.identity_verification_backend.dto;
// package com.dev.identity_verification_backend;
import lombok.Data;

@Data
public class AccountCreationRequestDto {
    // Field names MUST match the JSON keys sent by the frontend
    private String email;
    private String password;
}