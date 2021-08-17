package com.dna.backend.DNABackend.sesconfig;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SESSender implements ContentSender {

    private final AmazonSimpleEmailServiceAsync amazonSimpleEmailServiceAsync;

    @Override
    public boolean sendMessage(String email, String code) {
        Message message = new Message()
                .withSubject(createContent("DNA 인증 메일입니다."))
                .withBody(new Body().withHtml(createContent(code)));

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(email))
                .withSource("dnadsm2021@gmail.com")
                .withMessage(message);

        return amazonSimpleEmailServiceAsync.sendEmailAsync(request).isDone();
    }

    private Content createContent(String text) {
        return new Content()
                .withCharset("UTF-8")
                .withData(text);
    }

}
