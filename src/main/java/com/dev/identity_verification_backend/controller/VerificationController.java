package com.dev.identity_verification_backend.controller; // FIXED package name

// FIXED imports for DTOs and Service
import com.dev.identity_verification_backend.dto.*;
import com.dev.identity_verification_backend.service.VerificationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map; // <--- ADDED missing import for Map

@RestController
@RequestMapping("/api/verify")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class VerificationController {

    private static final Logger log = LoggerFactory.getLogger(VerificationController.class);

    // VerificationService will be resolved correctly now
    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<?> initiateVerification(
            @RequestParam("documentFront") MultipartFile documentFront,
            @RequestParam(value = "documentBack", required = false) MultipartFile documentBack,
            @RequestParam("selfie") MultipartFile selfie) {

        log.info("Received request for /initiate");
        if (documentFront == null || documentFront.isEmpty()) {
             log.warn("Document front is missing or empty.");
             // Map is now resolved
             return ResponseEntity.badRequest().body(Map.of("message", "Document front file is required."));
        }
         if (selfie == null || selfie.isEmpty()) {
             log.warn("Selfie is missing or empty.");
             return ResponseEntity.badRequest().body(Map.of("message", "Selfie file is required."));
        }

        try {
            // Calls the correctly resolved service
            String verificationId = verificationService.initiateVerification(documentFront, documentBack, selfie);
            log.info("Verification initiated with ID: {}", verificationId);
            // VerificationResponseDto is now resolved
            return ResponseEntity.ok(new VerificationResponseDto(verificationId));
        } catch (Exception e) {
            log.error("Error during verification initiation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("message", "An internal error occurred during verification initiation."));
        }
    }

    @GetMapping("/status/{verificationId}")
    public ResponseEntity<?> getVerificationStatus(@PathVariable String verificationId) {
         log.info("Received request for /status/{}", verificationId);
         if (verificationId == null || verificationId.trim().isEmpty()) {
             return ResponseEntity.badRequest().body(Map.of("message", "Verification ID is required."));
         }

        try {
            String status = verificationService.getVerificationStatus(verificationId);
            if ("NOT_FOUND".equals(status)) {
                log.warn("Status requested for non-existent ID: {}", verificationId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(Map.of("message", "Verification ID not found."));
            }
            log.info("Returning status '{}' for ID {}", status, verificationId);
            // StatusResponseDto is now resolved
            return ResponseEntity.ok(new StatusResponseDto(status));
        } catch (Exception e) {
             log.error("Error fetching status for ID {}", verificationId, e);
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                  .body(Map.of("message", "An internal error occurred while fetching status."));
        }
    }

    @PostMapping("/create-account")
    // AccountCreationRequestDto is now resolved
    public ResponseEntity<?> createAccount(@RequestBody AccountCreationRequestDto requestDto) {
        log.info("Received request for /create-account for email: {}", requestDto.getEmail());
        if (requestDto.getEmail() == null || requestDto.getPassword() == null ||
            requestDto.getEmail().isBlank() || requestDto.getPassword().isBlank()) {
            log.warn("Account creation request missing email or password.");
            return ResponseEntity.badRequest().body(Map.of("message", "Email and password are required."));
        }

        try {
            // AccountDto is now resolved
            AccountDto createdAccount = verificationService.createAccount(requestDto);
            if (createdAccount == null) {
                 log.warn("Account creation failed for {}: email likely already exists.", requestDto.getEmail());
                 return ResponseEntity.status(HttpStatus.CONFLICT)
                                      .body(Map.of("message", "Email address already exists."));
             }
            log.info("Account created successfully for {}", requestDto.getEmail());
            // AccountCreationResponseDto is now resolved
            AccountCreationResponseDto response = new AccountCreationResponseDto(
                    "Account created successfully.",
                    createdAccount
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
             log.error("Error during account creation for {}", requestDto.getEmail(), e);
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                  .body(Map.of("message", "An internal error occurred during account creation."));
        }
    }
}