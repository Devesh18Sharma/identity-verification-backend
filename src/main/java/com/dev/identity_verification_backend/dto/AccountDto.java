package com.dev.identity_verification_backend.dto; // FIXED package name

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String email;
    private String userId; // Example generated ID
}