package com.dna.backend.DNABackend.domain;

import com.dna.backend.DNABackend.DnaBackendApplication;
import com.dna.backend.DNABackend.entity.comment.CommentRepository;
import com.dna.backend.DNABackend.entity.timeline.Timeline;
import com.dna.backend.DNABackend.entity.timeline.TimelineRepository;
import com.dna.backend.DNABackend.entity.timeline.enums.Type;
import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.payload.request.TimelineRequest;
import com.dna.backend.DNABackend.payload.response.TimelineListResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = DnaBackendApplication.class)
public class TimelineControllerTest {

    @Autowired
    private TimelineRepository timelineRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @AfterEach
    public void cleanUp() {
        System.out.println("deleting..");
        commentRepository.deleteAll();
        timelineRepository.deleteAll();
        userRepository.deleteAll();
        System.out.println("finish cleaning!");
    }

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        userRepository.save(
            User.builder()
                    .email("email2@dsm.hs.kr")
                    .password("password")
                    .name("name")
                    .build()
        );

        userRepository.save(
            User.builder()
                    .email("email3@dsm.hs.kr")
                    .password("password")
                    .name("name")
                    .build()
        );
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
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        Timeline timeline = timelineRepository.findAll().get(0);

        Assertions.assertEquals(timeline.getTitle(),"titleTest");
        Assertions.assertEquals(timeline.getContent(),"contentTest");
    }

    @Test
    public void createTimelineTest_NoData() throws Exception {
        TimelineRequest request = TimelineRequest.builder()
                .content("")
                .title("")
                .type(Type.WORKER)
                .build();

        mvc.perform(post("/timeline")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getTimelineTest() throws Exception {
        Long timelineId = createTimeline(Type.COMMON, "success");
        createTimeline(Type.COMMON, "failure");
        createTimeline(Type.WORKER, "failure");

        MvcResult result = mvc.perform(get("/timeline/"+Type.COMMON)
                .param("size","10")
                .param("page","0")).andDo(print())
                .andExpect(status().isOk())
                .andReturn();
//
//        TimelineListResponse response = new ObjectMapper().readValue(result.getResponse().getContentAsString(), TimelineListResponse.class);
//
//        Assertions.assertEquals(response.getTotalElements(),2);
//        Assertions.assertEquals(response.getTimelineResponses().get(0).getTitle(),"success");

    }

    @Test
    public void deleteTimelineTest() throws Exception {
        Long timelineId = createTimeline(Type.COMMON, "if you see this title, delete failed");
        createTimeline(Type.COMMON, "success");
        createTimeline(Type.WORKER, "success");

        mvc.perform(delete("/timeline/"+timelineId))
                .andExpect(status().isOk());

        List<Timeline> timelines = timelineRepository.findAll();

        Assertions.assertEquals(timelines.size(),2);
        Assertions.assertEquals(timelines.get(0).getTitle(),"success");
    }

    public Long createTimeline(Type type, String title) {

        return timelineRepository.save(
                Timeline.builder()
                        .type(type)
                        .user(userRepository.findById("email2@dsm.hs.kr").get())
                        .content("content1")
                        .createdAt(LocalDateTime.now())
                        .title(title)
                        .build()
        ).getId();
    }
}
