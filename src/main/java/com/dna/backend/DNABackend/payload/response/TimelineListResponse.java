package com.dna.backend.DNABackend.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TimelineListResponse extends PageResponse {
    private List<TimelineResponse> timelineResponses;

    @Builder
    public TimelineListResponse(int totalElements, int totalPages, List<TimelineResponse> timelineResponses) {
        super(totalElements, totalPages);
        this.timelineResponses = timelineResponses;
    }
}
