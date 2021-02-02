package com.dna.backend.DNABackend.controller;

import com.dna.backend.DNABackend.entity.timeline.enums.Type;
import com.dna.backend.DNABackend.payload.request.TimelineRequest;
import com.dna.backend.DNABackend.payload.response.TimelineListResponse;
import com.dna.backend.DNABackend.service.timeline.TimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timeline")
public class TimelineController {

    private final TimelineService timelineService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTimeline(@RequestBody @Valid TimelineRequest timelineRequest) {
        timelineService.createTimeline(timelineRequest);
    }

    @GetMapping("/{type}")
    public TimelineListResponse getTimeline(@PathVariable Type type,
                                            Pageable page) {
        return timelineService.getTimelines(type, page);
    }

    @DeleteMapping("/{timelineId}")
    public void deleteTimeline(@PathVariable Long timelineId) {
        timelineService.deleteTimeline(timelineId);
    }

}
