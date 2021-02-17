package com.dna.backend.DNABackend.service.timeline;

import com.dna.backend.DNABackend.entity.comment.Comment;
import com.dna.backend.DNABackend.entity.comment.CommentRepository;
import com.dna.backend.DNABackend.entity.timeline.Timeline;
import com.dna.backend.DNABackend.entity.timeline.TimelineRepository;
import com.dna.backend.DNABackend.entity.timeline.enums.Type;
import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.exception.TimelineNotFoundException;
import com.dna.backend.DNABackend.exception.UserNotFoundException;
import com.dna.backend.DNABackend.payload.request.TimelineRequest;
import com.dna.backend.DNABackend.payload.response.LackOfPermissionException;
import com.dna.backend.DNABackend.payload.response.TimelineListResponse;
import com.dna.backend.DNABackend.payload.response.TimelineResponse;
import com.dna.backend.DNABackend.security.jwt.auth.AuthenticationFacade;
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
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;


    @Override
    public void createTimeline(TimelineRequest timelineRequest) {
        if(!authenticationFacade.isLogin()) {
            throw new UserNotFoundException();
        }

        User user = userRepository.findById(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        timelineRepository.save(timelineRequest.toEntity(user));

    }

    @Override
    public TimelineListResponse getTimelines(Type type, Pageable page) {
        User user = null;
        if(authenticationFacade.isLogin()) {
            user = userRepository.findByEmail(authenticationFacade.getUserEmail())
                    .orElseThrow(UserNotFoundException::new);
        }

        Page<Timeline> timelines;

        if(type == null) {
            timelines = timelineRepository.findAllByType(Type.WORKER, page);
        }else {
            timelines = timelineRepository.findAllByType(type, page);
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
                            .isMine(timeline.getUser().equals(user))
                            .timelineId(timeline.getId())
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
        if(!authenticationFacade.isLogin()) {
            throw new UserNotFoundException();
        }

        User user = userRepository.findById(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        Timeline timeline = timelineRepository.findById(timelineId)
                .orElseThrow(TimelineNotFoundException::new);

        if(!user.equals(timeline.getUser())) {
            throw new LackOfPermissionException();
        }

        for(Comment comment : timeline.getCommentList()) {
            commentRepository.delete(comment);
        }

        timelineRepository.delete(timeline);

    }


}
