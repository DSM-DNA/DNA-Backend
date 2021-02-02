package com.dna.backend.DNABackend.domain;

import com.dna.backend.DNABackend.DnaBackendApplication;
import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.payload.request.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DnaBackendApplication.class)
@ActiveProfiles("test")
class UserControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

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
    }

    @AfterEach
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Test
    public void 이메일중복확인_false() throws Exception {
        mvc.perform(get("/email")
                .param("email","byeMrHong@dsm.hs.kr"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 이메일중복확인_true() throws Exception {
        mvc.perform(get("/email")
                .param("email","byeMr@dsm.hs.kr"))
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인실패() throws Exception {
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
    public void 로그인성공() throws Exception {
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

}
