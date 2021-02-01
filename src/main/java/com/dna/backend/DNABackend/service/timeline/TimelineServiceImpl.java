package com.dna.backend.DNABackend.service.timeline;

import com.dna.backend.DNABackend.entity.comment.Comment;
import com.dna.backend.DNABackend.entity.comment.CommentRepository;
import com.dna.backend.DNABackend.entity.timeline.Timeline;
import com.dna.backend.DNABackend.entity.timeline.TimelineRepository;
import com.dna.backend.DNABackend.entity.timeline.enums.Type;
import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.exception.TimelineNotFoundException;
import com.dna.backend.DNABackend.payload.request.TimelineRequest;
import com.dna.backend.DNABackend.payload.response.TimelineListResponse;
import com.dna.backend.DNABackend.payload.response.TimelineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimelineServiceImpl implements TimelineService{

    private final TimelineRepository timelineRepository;
    private final CommentRepository commentRepository;


    @Override
    public void createTimeline(TimelineRequest timelineRequest) {
        // 대충 토큰검사 해
        User testUser = User.builder()
                .name("이름")
                .password("비밀번호")
                .email("이메일")
                .build();

        timelineRepository.save(timelineRequest.toEntity(testUser));

    }

    @Override
    public TimelineListResponse getTimelines(Type type, Pageable page) {

        User tmpUser = User.builder()
                .email("test@dsm.hs.kr")
                .password("password")
                .name("hong!")
                .build();

        Page<Timeline> timelines;
        if(type == null) {
            timelines = timelineRepository.findAllByType(type, page);
        }else {
            timelines = timelineRepository.findAllByType(Type.WORKER, page);
        }

        List<TimelineResponse> timelineResponse = new ArrayList<>();

        for(Timeline timeline : timelines) {
            timelineResponse.add(
                    TimelineResponse.builder()
                            .title(timeline.getTitle())
                            .content(timeline.getContent())
                            .name(timeline.getUser().getName())
                            .createdAt(timeline.getCreatedAt())
                            .type(timeline.getType())
                            .isMine(timeline.getUser().equals(tmpUser)) // 토큰 되면 진짜 유저가 올거야
                            .build()
            );
        }

        return TimelineListResponse.builder()
                .timelineResponses(timelineResponse)
                .totalElements((int) timelines.getTotalElements())
                .totalPages(timelines.getTotalPages())
                .build();

    }

    @Override
    public void deleteTimeline(Long timelineId) {
        // 대충 토큰검사 & 본인인지 확인
        Timeline timeline = timelineRepository.findById(timelineId)
                .orElseThrow(TimelineNotFoundException::new);

        for(Comment comment : timeline.getCommentList()) {
            commentRepository.delete(comment);
        }

        timelineRepository.delete(timeline);

    }


}
