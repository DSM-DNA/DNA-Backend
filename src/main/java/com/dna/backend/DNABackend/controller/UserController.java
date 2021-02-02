package com.dna.backend.DNABackend.controller;

import com.dna.backend.DNABackend.payload.request.SignInRequest;
import com.dna.backend.DNABackend.payload.request.SignUpRequest;
import com.dna.backend.DNABackend.payload.response.TokenResponse;
import com.dna.backend.DNABackend.service.auth.AuthService;
import com.dna.backend.DNABackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/email")
    public boolean emailCheck(@RequestParam String email) {
        return userService.confirmEmail(email);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
    }


    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse signIn(@RequestBody SignInRequest signInRequest) {
        return authService.signIn(signInRequest);
    }

}
