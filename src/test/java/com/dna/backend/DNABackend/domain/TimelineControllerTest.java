package com.dna.backend.DNABackend.domain;

import com.dna.backend.DNABackend.DnaBackendApplication;
import com.dna.backend.DNABackend.entity.comment.CommentRepository;
import com.dna.backend.DNABackend.entity.timeline.Timeline;
import com.dna.backend.DNABackend.entity.timeline.TimelineRepository;
import com.dna.backend.DNABackend.entity.timeline.enums.Type;
import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.payload.request.TimelineRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = DnaBackendApplication.class)
public class TimelineControllerTest {

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @AfterEach
    public void cleanUp() {
        timelineRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        User user = User.builder()
                .email("email@dsm.hs.kr")
                .password("password")
                .name("name")
                .build();

//        timelineRepository.save(
//                Timeline.builder()
//                        .type(Type.COMMON)
//                        .user(user)
//                        .content("content1")
//                        .createdAt(LocalDateTime.now())
//                        .title("title1")
//                        .build()
//        );
//
//        timelineRepository.save(
//                Timeline.builder()
//                        .type(Type.COMMON)
//                        .user(user)
//                        .content("content1")
//                        .createdAt(LocalDateTime.now())
//                        .title("title1")
//                        .build()
//        );

    }

    @Test
    public void createTimelineTest() throws Exception {
        TimelineRequest request = TimelineRequest.builder()
                .content("contentTest")
                .title("titleTest")
                .type(Type.WORKER)
                .build();

        mvc.perform(post("/timeline")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
                .andExpect(status().isCreated());

        Timeline timeline = timelineRepository.findAll().get(0);

        Assertions.assertEquals(timeline.getTitle(),"titleTest");
        Assertions.assertEquals(timeline.getContent(),"contentTest");
    }
}
