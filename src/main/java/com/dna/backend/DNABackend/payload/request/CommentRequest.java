package com.dna.backend.DNABackend.payload.request;

import com.dna.backend.DNABackend.entity.comment.Comment;
import com.dna.backend.DNABackend.entity.timeline.Timeline;
import com.dna.backend.DNABackend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotBlank
    private String content;

    @NotNull
    private Long timelineId;

    public Comment toEntity(Timeline timeline, User user) {
        return Comment.builder()
                .content(content)
                .timeline(timeline)
                .user(user)
                .build();
    }
}
