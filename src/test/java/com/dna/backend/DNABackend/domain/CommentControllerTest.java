package com.dna.backend.DNABackend.domain;

import com.dna.backend.DNABackend.DnaBackendApplication;
import com.dna.backend.DNABackend.entity.comment.Comment;
import com.dna.backend.DNABackend.entity.comment.CommentRepository;
import com.dna.backend.DNABackend.entity.timeline.Timeline;
import com.dna.backend.DNABackend.entity.timeline.TimelineRepository;
import com.dna.backend.DNABackend.entity.timeline.enums.Type;
import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.payload.request.CommentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DnaBackendApplication.class)
@ActiveProfiles("test")
public class CommentControllerTest {

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
        commentRepository.deleteAll();
        timelineRepository.deleteAll();
        userRepository.deleteAll();
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

        timelineRepository.save(
                Timeline.builder()
                        .type(Type.COMMON)
                        .user(userRepository.findById("email2@dsm.hs.kr").get())
                        .content("content1")
                        .createdAt(LocalDateTime.now())
                        .title("title")
                        .build()
        );

        timelineRepository.save(
                Timeline.builder()
                        .type(Type.WORKER)
                        .user(userRepository.findById("email2@dsm.hs.kr").get())
                        .content("content1")
                        .createdAt(LocalDateTime.now())
                        .title("title")
                        .build()
        );

        timelineRepository.save(
                Timeline.builder()
                        .type(Type.COMMON)
                        .user(userRepository.findById("email2@dsm.hs.kr").get())
                        .content("content1")
                        .createdAt(LocalDateTime.now())
                        .title("title")
                        .build()
        );
    }

    @Test
    @WithMockUser(value = "email2@dsm.hs.kr",password = "password")
    public void createCommentTest() throws Exception {
        Timeline timeline = timelineRepository.findAll().get(0);

        CommentRequest request = CommentRequest.builder()
                .content("content11")
                .timelineId(timeline.getId())
                .build();

        mvc.perform(post("/comment")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        Comment comment = commentRepository.findAll().get(0);

        Assertions.assertEquals(comment.getContent(),"content11");
        Assertions.assertEquals(comment.getTimeline().getTitle(), timeline.getTitle());
    }

    @Test
    @WithMockUser(value = "email2@dsm.hs.kr",password = "password")
    public void getCommentTest() throws Exception {
        Timeline timeline = timelineRepository.findAll().get(0);

        createComment(timeline.getId(),"expected content1");
        createComment(timeline.getId(),"expected content2");
        createComment(timeline.getId()+1, "not expected");

        mvc.perform(get("/comment/"+timeline.getId())
                .param("size","10")
                .param("page","0")).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "email2@dsm.hs.kr",password = "password")
    public void deleteCommentTest() throws Exception {
        Timeline timeline = timelineRepository.findAll().get(0);

        Long id = createComment(timeline.getId(),"not content1");
        createComment(timeline.getId(),"expected content");
        createComment(timeline.getId()+1, "expected expected");

        mvc.perform(delete("/comment/"+id))
                .andExpect(status().isOk());

        Assertions.assertEquals(commentRepository.findAll().get(0).getContent(),"expected content");
    }

    public Long createComment(Long timelineId, String content) {
        return commentRepository.save(
                Comment.builder()
                        .user(userRepository.findByEmail("email2@dsm.hs.kr").get())
                        .timeline(timelineRepository.findById(timelineId).get())
                        .content(content)
                        .build()
        ).getId();
    }

}
