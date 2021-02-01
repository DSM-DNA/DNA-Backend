package com.dna.backend.DNABackend.service.comment;

import com.dna.backend.DNABackend.payload.request.CommentRequest;
import com.dna.backend.DNABackend.payload.response.CommentListResponse;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    void createComment(CommentRequest commentRequest);

    void deleteComment(Long commentId);

    CommentListResponse getCommentList(Long timelineId, Pageable page);
}
