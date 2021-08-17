package com.dna.backend.DNABackend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class VerifyRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String verifyCode;

}
