package com.dna.backend.DNABackend.payload.request;

import com.dna.backend.DNABackend.entity.comment.Comment;
import com.dna.backend.DNABackend.entity.timeline.Timeline;
import com.dna.backend.DNABackend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    private String content;

    private Long timelineId;

    public Comment toEntity(Timeline timeline, User user) {
        return Comment.builder()
                .content(content)
                .timeline(timeline)
                .build();
    }
}
