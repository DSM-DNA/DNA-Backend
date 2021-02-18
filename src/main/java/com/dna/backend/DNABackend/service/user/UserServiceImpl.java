package com.dna.backend.DNABackend.service.user;

import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.exception.InvalidEmailAddressException;
import com.dna.backend.DNABackend.exception.UserAlreadyExistsException;
import com.dna.backend.DNABackend.payload.request.SignUpRequest;
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
            throw new InvalidEmailAddressException(); //학교 이메일인지 확인

        if (userRepository.existsById(email)) {
            throw new UserAlreadyExistsException(); //이메일이 이미 존재
        }else {
            return true; //이메일이 존재 하지 않음 -> 가입 가능
        }
    }

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        userRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException();
                });

        if (!User.isValidAddress(signUpRequest.getEmail()))
            throw new InvalidEmailAddressException();

        userRepository.save(
                User.builder()
                        .name(signUpRequest.getName())
                        .email(signUpRequest.getEmail())
                        .password(passwordEncoder.encode(signUpRequest.getPassword()))
                        .build()
        );

    }
}
