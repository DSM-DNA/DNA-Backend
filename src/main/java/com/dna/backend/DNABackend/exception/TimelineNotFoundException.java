package com.dna.backend.DNABackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Timeline Not Found")
public class TimelineNotFoundException extends RuntimeException{
}
