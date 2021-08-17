package com.dna.backend.DNABackend.controller;

import com.dna.backend.DNABackend.payload.request.EmailRequest;
import com.dna.backend.DNABackend.payload.request.VerifyRequest;
import com.dna.backend.DNABackend.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public void sendVerifyCode(EmailRequest request) {
        emailService.sendVerifyCode(request);
    }

    @PatchMapping
    public void verify(VerifyRequest request) {
        emailService.verify(request);
    }

}
