package com.dna.backend.DNABackend.service.auth;

import com.dna.backend.DNABackend.payload.request.SignInRequest;

public interface AuthService {
    void signIn(SignInRequest signInRequest);
}
