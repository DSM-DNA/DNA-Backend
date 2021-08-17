package com.dna.backend.DNABackend.sesconfig;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsSesConfiguration {

    @Value("${aws.ses.access-key}")
    private String AWS_ACCESS_KEY;

    @Value("${aws.ses.secret-key}")
    private String AWS_SECRET_KEY;

    @Bean
    public AmazonSimpleEmailServiceAsync amazonSimpleEmailServiceAsync() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);

        return AmazonSimpleEmailServiceAsyncClient.asyncBuilder()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("ap-northeast-2")
                .build();
    }

}
