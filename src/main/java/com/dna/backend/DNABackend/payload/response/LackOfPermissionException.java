package com.dna.backend.DNABackend.payload.response;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User Permission Lack")
public class LackOfPermissionException extends RuntimeException{
}
