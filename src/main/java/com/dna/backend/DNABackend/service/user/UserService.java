package com.dna.backend.DNABackend.service.user;

import com.dna.backend.DNABackend.payload.request.SignUpRequest;

public interface UserService {
    boolean confirmEmail(String email);
    void signUp(SignUpRequest signUpRequest);
}
