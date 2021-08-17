package com.dna.backend.DNABackend.service.email;

import com.dna.backend.DNABackend.payload.request.EmailRequest;
import com.dna.backend.DNABackend.payload.request.VerifyRequest;

public interface EmailService {
    void sendVerifyCode(EmailRequest request);
    void verify(VerifyRequest request);
}
