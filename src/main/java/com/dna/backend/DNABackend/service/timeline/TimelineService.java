package com.dna.backend.DNABackend.service.timeline;

import com.dna.backend.DNABackend.entity.timeline.enums.Type;
import com.dna.backend.DNABackend.payload.request.TimelineRequest;
import com.dna.backend.DNABackend.payload.response.TimelineListResponse;
import org.springframework.data.domain.Pageable;

public interface TimelineService {
    void createTimeline(TimelineRequest timelineRequest);

    TimelineListResponse getTimelines(Type type, Pageable page);

    void deleteTimeline(Long timelineId);
}
