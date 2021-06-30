package com.dna.backend.DNABackend.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_exp")
    private Long refreshExp;

    @JsonProperty("access-token")
    private String accessToken2;

    @JsonProperty("refresh-token")
    private String refreshToken2;

    @JsonProperty("refresh-exp")
    private Long refreshExp2;

}
