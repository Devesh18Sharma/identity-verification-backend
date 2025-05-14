package com.dev.identity_verification_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationResponseDto {
    private String message;
    private AccountDto account; // Embed the account details
}