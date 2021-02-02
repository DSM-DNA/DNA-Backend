package com.dna.backend.DNABackend.service.user;

import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.exception.InvalidEmailAddressException;
import com.dna.backend.DNABackend.exception.UserAlreadyExistsException;
import com.dna.backend.DNABackend.payload.request.SignUpRequest;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean confirmEmail(String email) {
        if (!User.isValidAddress(email))
            throw new InvalidEmailAddressException();

        if (userRepository.existsById(email)) {
            throw new UserAlreadyExistsException();
        }else {
            return true;
        }
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        userRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException();
                });

        userRepository.save(
                User.builder()
                        .name(signUpRequest.getName())
                        .email(signUpRequest.getEmail())
                        .password(passwordEncoder.encode(signUpRequest.getPassword()))
                        .build()
        );

    }
}
