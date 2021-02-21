package com.dna.backend.DNABackend.entity.timeline;

import com.dna.backend.DNABackend.entity.timeline.enums.Type;
import com.dna.backend.DNABackend.payload.response.TimelineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimelineRepository extends JpaRepository<Timeline, Long> {

    Page<Timeline> findAllByTypeOrderByCreatedAtDesc(Type type, Pageable pageable);

}
