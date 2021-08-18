package com.dna.backend.DNABackend.service.email;

import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.entity.verify.VerifyCode;
import com.dna.backend.DNABackend.entity.verify.VerifyCodeRepository;
import com.dna.backend.DNABackend.exception.VerifyCodeNotMatchException;
import com.dna.backend.DNABackend.payload.request.EmailRequest;
import com.dna.backend.DNABackend.payload.request.VerifyRequest;
import com.dna.backend.DNABackend.sesconfig.ContentSender;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private static final Random RANDOM = new Random();
    private final UserRepository userRepository;
    private final VerifyCodeRepository verifyCodeRepository;
    private final JavaMailSender javaMailSender;
    private final ContentSender contentSender;

    @Async
    @Override
    public void sendVerifyCode(EmailRequest request) {
        String authNum = generateVerifyCode();

        try {
            final MimeMessagePreparator preparator = mimeMessage -> {
                final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setFrom("dna@gmail.com");
                helper.setTo(request.getEmail());
                helper.setSubject("DNA 인증번호");
                helper.setText("인증번호는 \" + authNum + \" 입니다");
            };

            javaMailSender.send(preparator);

            verifyCodeRepository.findById(request.getEmail())
                    .ifPresent(verifyCodeRepository::delete);

            verifyCodeRepository.save(
                    VerifyCode.builder()
                            .email(request.getEmail())
                            .verifyCode(authNum)
                            .isVerified(false)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String email = request.getEmail();
//        if (!User.isValidAddress(email))
//            throw new InvalidEmailAddressException();
//
//        if (userRepository.existsById(email)) {
//            throw new UserAlreadyExistsException();
//        }
//
//        verifyCodeRepository.findById(email)
//                .ifPresent(verifyCodeRepository::delete);
//
//        contentSender.sendMessage(email, generateVerifyCode());
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
