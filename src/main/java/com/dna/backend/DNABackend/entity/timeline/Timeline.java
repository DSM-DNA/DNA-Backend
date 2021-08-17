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
import org.springframework.format.annotation.DateTimeFormat;
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

    @DateTimeFormat(pattern = "yyyy-MM-dd`T`hh:mm:SS")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "timeline")
    private List<Comment> commentList;

}
