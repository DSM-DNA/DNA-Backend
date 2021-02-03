package com.dna.backend.DNABackend.service.comment;

import com.dna.backend.DNABackend.entity.comment.Comment;
import com.dna.backend.DNABackend.entity.comment.CommentRepository;
import com.dna.backend.DNABackend.entity.timeline.Timeline;
import com.dna.backend.DNABackend.entity.timeline.TimelineRepository;
import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.entity.user.UserRepository;
import com.dna.backend.DNABackend.exception.CommentNotFoundException;
import com.dna.backend.DNABackend.exception.TimelineNotFoundException;
import com.dna.backend.DNABackend.exception.UserNotFoundException;
import com.dna.backend.DNABackend.payload.request.CommentRequest;
import com.dna.backend.DNABackend.payload.response.CommentListResponse;
import com.dna.backend.DNABackend.payload.response.CommentResponse;
import com.dna.backend.DNABackend.payload.response.LackOfPermissionException;
import com.dna.backend.DNABackend.security.jwt.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TimelineRepository timelineRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;

    @Override
    public void createComment(CommentRequest commentRequest) {
        if(!authenticationFacade.isLogin()) {
            throw new UserNotFoundException();
        }

        User user = userRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        Timeline timeline = timelineRepository.findById(commentRequest.getTimelineId())
                .orElseThrow(TimelineNotFoundException::new);


        commentRepository.save(commentRequest.toEntity(timeline,user)); // 나중엔 진짜 user!
    }

    @Override
    public void deleteComment(Long commentId) {
        if(!authenticationFacade.isLogin()) {
            throw new UserNotFoundException();
        }

        User user = userRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if(!comment.getUser().equals(user)) {
            throw new LackOfPermissionException();
        }
        commentRepository.delete(comment);
    }

    @Override
    public CommentListResponse getCommentList(Long timelineId, Pageable page) {
        User user = null;
        if(authenticationFacade.isLogin()) {
            user = userRepository.findByEmail(authenticationFacade.getUserEmail())
                    .orElseThrow(UserNotFoundException::new);
        }

        Timeline timeline = timelineRepository.findById(timelineId)
                .orElseThrow(TimelineNotFoundException::new);

        Page<Comment> comments = commentRepository.findAllByTimeline(timeline,page);
        List<CommentResponse> commentResponses = new ArrayList<>();

        for(Comment comment : comments) {
            commentResponses.add(
                    CommentResponse.builder()
                            .content(comment.getContent())
                            .isMine(comment.getUser().equals(user)) // 토큰 생기면 추가할걸?
                            .build()
            );
        }

        return CommentListResponse.builder()
                .commentResponses(commentResponses)
                .totalElements((int) comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .build();

    }
}
