package com.dna.backend.DNABackend.controller;

import com.dna.backend.DNABackend.payload.request.EmailRequest;
import com.dna.backend.DNABackend.payload.request.VerifyRequest;
import com.dna.backend.DNABackend.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void sendVerifyCode(@RequestBody EmailRequest request) {
        emailService.sendVerifyCode(request);
    }

    @PatchMapping
    public void verify(@RequestBody VerifyRequest request) {
        emailService.verify(request);
    }

}
