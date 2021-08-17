package com.dna.backend.DNABackend.domain;

import com.dna.backend.DNABackend.DnaBackendApplication;
import com.dna.backend.DNABackend.entity.refreshToken.RefreshToken;
import com.dna.backend.DNABackend.entity.refreshToken.RefreshTokenRepository;
import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.entity.verify.VerifyCode;
import com.dna.backend.DNABackend.entity.verify.VerifyCodeRepository;
import com.dna.backend.DNABackend.payload.request.SignInRequest;
import com.dna.backend.DNABackend.payload.request.SignUpRequest;
import com.dna.backend.DNABackend.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DnaBackendApplication.class)
@ActiveProfiles("test")
class UserControllerTest {

    String accessToken;
    String refreshToken;
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private VerifyCodeRepository verifyCodeRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        accessToken = jwtTokenProvider.generateAccessToken("helloMrGo@dsm.hs.kr");
        refreshToken = jwtTokenProvider.generateRefreshToken("helloMrGo@dsm.hs.kr");

        verifyCodeRepository.save(
                    new VerifyCode("byeMrHong-@dsm.hs.kr", "123456", true)
        );

        userRepository.save(
                User.builder()
                        .email("byeMrHong@dsm.hs.kr")
                        .name("홍길동")
                        .password(passwordEncoder.encode("1234"))
                        .build()
        );
        userRepository.save(
                User.builder()
                        .email("helloMrGo@dsm.hs.kr")
                        .name("고길동")
                        .password(passwordEncoder.encode("1234"))
                        .build()
        );
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .email("helloMrGo@dsm.hs.kr")
                        .refreshToken(refreshToken)
                        .refreshExp(500L)
                        .build()
        );
    }

    @AfterEach
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Test
    public void 회원가입실패() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("홍정현")
                .email("byeMrHong@dsm.hs.kr")
                .password("1234")
                .build();

        mvc.perform(post("/signup")
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입성공() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .name("홍정현")
                .email("byeMrHong-@dsm.hs.kr")
                .password("1234")
                .build();
        mvc.perform(post("/signup")
                        .content(new ObjectMapper().writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void 로그인성공() throws Exception {
        SignInRequest signInRequest = new SignInRequest("byeMrHong@dsm.hs.kr", "1234");

        mvc.perform(post("/auth")
                        .content(new ObjectMapper().writeValueAsString(signInRequest))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void refreshTokenTest() throws Exception {
        mvc.perform(put("/auth")
                .header("X-Refresh-Token", refreshToken)
        ).andExpect(status().isOk());
    }

    @Test
    public void refreshTokenTestWithExpect() throws Exception {
        mvc.perform(put("/auth")
                .header("X-Refresh-Token", "apple")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void refreshTokenTestWithIsNotRefreshTokenExcept() throws Exception {
        mvc.perform(put("/auth")
                .header("X-Refresh-Token", accessToken)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "helloMrGo@dsm.hs.kr", password = "1234")
    public void 로그아웃() throws Exception {
        mvc.perform(get("/logout")
        ).andExpect(status().isOk());
    }

    @Test
    public void 로그아웃_실패() throws Exception {
        mvc.perform(get("/logout")
        ).andExpect(status().isBadRequest()).andDo(print());
    }


}
