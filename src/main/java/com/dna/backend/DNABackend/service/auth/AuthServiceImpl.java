package com.dna.backend.DNABackend.service.auth;

import com.dna.backend.DNABackend.entity.refreshToken.RefreshToken;
import com.dna.backend.DNABackend.entity.refreshToken.RefreshTokenRepository;
import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.exception.InvalidTokenException;
import com.dna.backend.DNABackend.exception.UserNotFoundException;
import com.dna.backend.DNABackend.payload.request.SignInRequest;
import com.dna.backend.DNABackend.payload.response.AccessTokenResponse;
import com.dna.backend.DNABackend.payload.response.TokenResponse;
import com.dna.backend.DNABackend.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${auth.jwt.exp.refresh}")
    private Long refreshExp;

    @Value("${auth.jwt.exp.access}")
    private Long accessExp;

    @Override
    public TokenResponse signIn(SignInRequest signInRequest) {
        return userRepository.findByEmail(signInRequest.getEmail())
                .filter(user -> passwordEncoder.matches(signInRequest.getPassword(), user.getPassword()))
                .map(User::getEmail)
                .map(email -> {
                    String refreshToken = jwtTokenProvider.generateRefreshToken(email);
                    return new RefreshToken(email, refreshToken, refreshExp);
                })
                .map(refreshTokenRepository::save)
                .map(refreshToken -> {
                    String accessToken = jwtTokenProvider.generateAccessToken(refreshToken.getEmail());
                    return new TokenResponse(accessToken, refreshToken.getRefreshToken(), accessExp);
                })
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public AccessTokenResponse tokenRefresh(String receivedToken) {
        if(!jwtTokenProvider.isRefreshToken(receivedToken)) {
            throw new InvalidTokenException();
        }

        return refreshTokenRepository.findByRefreshToken(receivedToken)
                .map(refreshToken -> refreshToken.update(refreshExp))
                .map(refreshTokenRepository::save)
                .map(refreshToken ->
                        new AccessTokenResponse(jwtTokenProvider.generateAccessToken(jwtTokenProvider.getEmail(receivedToken))))
                .orElseThrow(InvalidTokenException::new);
    }

}
