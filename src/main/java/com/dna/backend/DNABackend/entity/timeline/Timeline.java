package com.dna.backend.DNABackend.entity.timeline;

import com.dna.backend.DNABackend.entity.comment.Comment;
import com.dna.backend.DNABackend.entity.timeline.enums.Type;
import com.dna.backend.DNABackend.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_timeline")
public class Timeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "timeline")
    @JsonBackReference
    private List<Comment> commentList;

}
