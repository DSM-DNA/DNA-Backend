package com.dna.backend.DNABackend.payload.response;

import com.dna.backend.DNABackend.entity.timeline.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimelineResponse {

    private String title;

    private String content;

    private Type type;

    private String name;

    private Boolean isMine;

    private LocalDateTime createdAt;

}
