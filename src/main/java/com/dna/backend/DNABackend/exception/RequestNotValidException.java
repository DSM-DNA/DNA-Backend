package com.dna.backend.DNABackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Request is not valid")
public class RequestNotValidException extends RuntimeException {
}
