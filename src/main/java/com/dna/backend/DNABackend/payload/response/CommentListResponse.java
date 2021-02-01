package com.dna.backend.DNABackend.payload.response;

import com.dna.backend.DNABackend.entity.comment.Comment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentListResponse extends PageResponse{

    private List<CommentResponse> commentResponses;

    @Builder
    public CommentListResponse(int totalElements, int totalPages, List<CommentResponse> commentResponses) {
        super(totalElements,totalPages);
        this.commentResponses = commentResponses;
    }

}
