package com.dna.backend.DNABackend.service.email;

import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.entity.verify.VerifyCode;
import com.dna.backend.DNABackend.entity.verify.VerifyCodeRepository;
import com.dna.backend.DNABackend.exception.InvalidEmailAddressException;
import com.dna.backend.DNABackend.exception.UserAlreadyExistsException;
import com.dna.backend.DNABackend.exception.VerifyCodeNotMatchException;
import com.dna.backend.DNABackend.payload.request.EmailRequest;
import com.dna.backend.DNABackend.payload.request.VerifyRequest;
import com.dna.backend.DNABackend.sesconfig.ContentSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final UserRepository userRepository;
    private final VerifyCodeRepository verifyCodeRepository;

    private final ContentSender contentSender;

    private static final Random RANDOM = new Random();

    @Override
    public void sendVerifyCode(EmailRequest request) {
        String email = request.getEmail();
        if (!User.isValidAddress(email))
            throw new InvalidEmailAddressException();

        if (userRepository.existsById(email)) {
            throw new UserAlreadyExistsException();
        }

        verifyCodeRepository.findById(email)
                .ifPresent(verifyCodeRepository::delete);

        contentSender.sendMessage(email, generateVerifyCode());
    }

    @Override
    public void verify(VerifyRequest request) {
        verifyCodeRepository.findById(request.getEmail())
                .filter(verifyCode -> verifyCode.getVerifyCode().equals(request.getVerifyCode()))
                .map(VerifyCode::isVerifiedTrue)
                .map(verifyCodeRepository::save)
                .orElseThrow(VerifyCodeNotMatchException::new);
    }

    private String generateVerifyCode() {
        RANDOM.setSeed(System.currentTimeMillis());
        return Integer.toString(RANDOM.nextInt(1000000));
    }

}
