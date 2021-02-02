package com.dna.backend.DNABackend.service.user;

import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.payload.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public boolean confirmEmail(String email) {
        if (userRepository.existsById(email)) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) {

    }
}
