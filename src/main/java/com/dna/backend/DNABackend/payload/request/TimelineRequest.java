package com.dna.backend.DNABackend.payload.request;

import com.dna.backend.DNABackend.entity.timeline.Timeline;
import com.dna.backend.DNABackend.entity.timeline.enums.Type;
import com.dna.backend.DNABackend.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimelineRequest {

    private String title;

    private String content;

    private Type type;

    public Timeline toEntity(User user) {
        return Timeline.builder()
                .type(type)
                .content(content)
                .title(title)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
    }

}
