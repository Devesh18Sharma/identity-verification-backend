package com.dev.identity_verification_backend.service; // FIXED package name

// FIXED imports to use the correct dto package
import com.dev.identity_verification_backend.dto.AccountCreationRequestDto;
import com.dev.identity_verification_backend.dto.AccountDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map; // Keep this import
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class VerificationService {

    private static final Logger log = LoggerFactory.getLogger(VerificationService.class);

    private final Map<String, String> verificationStatuses = new ConcurrentHashMap<>();
    private final Map<String, AccountDto> createdAccounts = new ConcurrentHashMap<>();

    public String initiateVerification(MultipartFile documentFront, MultipartFile documentBack, MultipartFile selfie) {
        log.info("Simulating verification initiation...");
        log.info("Received Document Front: {} ({} bytes)", documentFront.getOriginalFilename(), documentFront.getSize());
        if (documentBack != null && !documentBack.isEmpty()) {
            log.info("Received Document Back: {} ({} bytes)", documentBack.getOriginalFilename(), documentBack.getSize());
        }
        log.info("Received Selfie: {} ({} bytes)", selfie.getOriginalFilename(), selfie.getSize());

        String verificationId = "sim-" + UUID.randomUUID().toString();
        verificationStatuses.put(verificationId, "PENDING");
        log.info("Generated simulated Verification ID: {}", verificationId);
        scheduleStatusChange(verificationId);
        return verificationId;
    }

    public String getVerificationStatus(String verificationId) {
        log.info("Checking status for Verification ID: {}", verificationId);
        String status = verificationStatuses.getOrDefault(verificationId, "NOT_FOUND");
        if ("NOT_FOUND".equals(status)) {
            log.warn("Verification ID {} not found in simulated store.", verificationId);
        } else {
             log.info("Current simulated status for {}: {}", verificationId, status);
         }
         return status;
    }

     public AccountDto createAccount(AccountCreationRequestDto request) {
         log.info("Simulating account creation for email: {}", request.getEmail());
         if (createdAccounts.values().stream().anyMatch(acc -> acc.getEmail().equalsIgnoreCase(request.getEmail()))) {
              log.warn("Simulated error: Email {} already exists.", request.getEmail());
              return null; // Indicate failure due to existing email
         }
         String userId = "user-" + UUID.randomUUID().toString().substring(0, 8);
         // AccountDto will be found correctly now
         AccountDto newAccount = new AccountDto(request.getEmail(), userId);
         createdAccounts.put(userId, newAccount);
         log.info("Simulated account created with User ID: {}", userId);
         return newAccount;
     }

    private void scheduleStatusChange(String verificationId) {
        new Thread(() -> {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 8000));
                if ("PENDING".equals(verificationStatuses.get(verificationId))) {
                    verificationStatuses.put(verificationId, "PROCESSING");
                    log.info("Simulated status for {} changed to: PROCESSING", verificationId);
                }
                Thread.sleep(ThreadLocalRandom.current().nextInt(5000, 15000));
                 if ("PROCESSING".equals(verificationStatuses.get(verificationId))) {
                     String finalStatus = ThreadLocalRandom.current().nextBoolean() ? "APPROVED" : "REJECTED";
                     verificationStatuses.put(verificationId, finalStatus);
                     log.info("Simulated status for {} changed to: {}", verificationId, finalStatus);
                 }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Status change simulation interrupted for {}", verificationId, e);
            }
        }).start();
    }
}