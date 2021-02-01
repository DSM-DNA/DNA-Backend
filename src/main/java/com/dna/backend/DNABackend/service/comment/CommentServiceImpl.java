package com.dna.backend.DNABackend.service.comment;

import com.dna.backend.DNABackend.entity.comment.Comment;
import com.dna.backend.DNABackend.entity.comment.CommentRepository;
import com.dna.backend.DNABackend.entity.timeline.Timeline;
import com.dna.backend.DNABackend.entity.timeline.TimelineRepository;
import com.dna.backend.DNABackend.entity.user.User;
import com.dna.backend.DNABackend.exception.CommentNotFoundException;
import com.dna.backend.DNABackend.exception.TimelineNotFoundException;
import com.dna.backend.DNABackend.payload.request.CommentRequest;
import com.dna.backend.DNABackend.payload.response.CommentListResponse;
import com.dna.backend.DNABackend.payload.response.CommentResponse;
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

    User tmpUser = User.builder()
            .email("email@dsm.hs.kr")
            .password("password")
            .name("name!")
            .build();

    @Override
    public void createComment(CommentRequest commentRequest) {
        // 토큰 유무 검사

        Timeline timeline = timelineRepository.findById(commentRequest.getTimelineId())
                .orElseThrow(TimelineNotFoundException::new);


        commentRepository.save(commentRequest.toEntity(timeline,tmpUser)); // 나중엔 진짜 user!
    }

    @Override
    public void deleteComment(Long commentId) {
        // 토큰 유무 검사
        // 본인 댓글인지 검사
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        commentRepository.delete(comment);
    }

    @Override
    public CommentListResponse getCommentList(Long timelineId, Pageable page) {
        // 토큰 값 있으면 user에 값 넣고 없으면 말구

        Timeline timeline = timelineRepository.findById(timelineId)
                .orElseThrow(TimelineNotFoundException::new);

        Page<Comment> comments = commentRepository.findAllByTimeline(timeline,page);
        List<CommentResponse> commentResponses = new ArrayList<>();

        for(Comment comment : comments) {
            commentResponses.add(
                    CommentResponse.builder()
                            .content(comment.getContent())
                            .isMine(comment.getUser().equals(tmpUser)) // 토큰 생기면 추가할걸?
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
