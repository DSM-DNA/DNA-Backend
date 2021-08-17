package com.dna.backend.DNABackend.service.user;

import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.entity.verify.VerifyCode;
import com.dna.backend.DNABackend.entity.verify.VerifyCodeRepository;
import com.dna.backend.DNABackend.exception.InvalidEmailAddressException;
import com.dna.backend.DNABackend.exception.UserAlreadyExistsException;
import com.dna.backend.DNABackend.exception.UserNotAccessibleException;
import com.dna.backend.DNABackend.payload.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final VerifyCodeRepository verifyCodeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        verifyCodeRepository.findById(signUpRequest.getEmail())
                .filter(VerifyCode::isVerified)
                .orElseThrow(UserNotAccessibleException::new);

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
