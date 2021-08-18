package com.dna.backend.DNABackend.entity.verify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(timeToLive = 60 * 4)
public class VerifyCode {

    @Id
    private String email;

    private String verifyCode;

    private boolean isVerified;

    public VerifyCode isVerifiedTrue() {
        this.isVerified = true;
        return this;
    }

}
