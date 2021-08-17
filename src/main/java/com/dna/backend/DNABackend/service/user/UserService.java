package com.dna.backend.DNABackend.service.user;

import com.dna.backend.DNABackend.payload.request.SignUpRequest;

public interface UserService {
    void signUp(SignUpRequest signUpRequest);
}
