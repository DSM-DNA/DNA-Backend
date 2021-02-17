package com.dna.backend.DNABackend.service.auth;

import com.dna.backend.DNABackend.payload.request.SignInRequest;
import com.dna.backend.DNABackend.payload.response.AccessTokenResponse;
import com.dna.backend.DNABackend.payload.response.TokenResponse;

public interface AuthService {
    TokenResponse signIn(SignInRequest signInRequest);
    AccessTokenResponse tokenRefresh(String receivedToken);
}
