package com.dna.backend.DNABackend.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccessTokenResponse {

    @JsonProperty("access_token")
    private final String accessToken;

    @JsonProperty("access-token")
    private final String accessToken2;

}
