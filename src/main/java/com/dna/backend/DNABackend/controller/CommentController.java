package com.dna.backend.DNABackend.controller;

import com.dna.backend.DNABackend.payload.request.CommentRequest;
import com.dna.backend.DNABackend.payload.response.CommentListResponse;
import com.dna.backend.DNABackend.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{timelineId}")
    public CommentListResponse getComment(@PathVariable Long timelineId,
                                          Pageable page) {
        return commentService.getCommentList(timelineId, page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment(@RequestBody CommentRequest commentRequest) {
        commentService.createComment(commentRequest);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
