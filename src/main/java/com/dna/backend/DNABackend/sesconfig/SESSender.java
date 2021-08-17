package com.dna.backend.DNABackend.sesconfig;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class SESSender implements ContentSender {

    private final ObjectMapper objectMapper;
    private final AmazonSimpleEmailServiceAsync amazonSimpleEmailServiceAsync;

    @Override
    public boolean sendMessage(String email, Map<String, String> params) {
        SendTemplatedEmailRequest request = new SendTemplatedEmailRequest()
                .withDestination(new Destination().withToAddresses(email))
                .withSource("<dnadsm2021@gmail.com>")
                .withTemplateData(paramToJson(params));

        return amazonSimpleEmailServiceAsync.sendTemplatedEmailAsync(request).isDone();
    }

    @SneakyThrows
    private String paramToJson(Map<String, String> params) {
        return objectMapper.writeValueAsString(params).replaceAll("\"", "\\\"");
    }

}
