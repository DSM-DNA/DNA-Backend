package com.dna.backend.DNABackend.controller;

import com.dna.backend.DNABackend.exception.RequestNotValidException;
import com.dna.backend.DNABackend.payload.request.SignInRequest;
import com.dna.backend.DNABackend.payload.request.SignUpRequest;
import com.dna.backend.DNABackend.payload.response.AccessTokenResponse;
import com.dna.backend.DNABackend.payload.response.TokenResponse;
import com.dna.backend.DNABackend.service.auth.AuthService;
import com.dna.backend.DNABackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        else {
            throw new RequestNotValidException();
        }
    }

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse signIn(@RequestBody SignInRequest signInRequest) {
        return authService.signIn(signInRequest);
    }

    @PutMapping("/auth")
    public AccessTokenResponse tokenRefresh(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return authService.tokenRefresh(refreshToken);
    }

}
