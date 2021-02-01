package com.dna.backend.DNABackend.entity.user;

import com.dna.backend.DNABackend.entity.comment.Comment;
import com.dna.backend.DNABackend.entity.timeline.Timeline;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    private String email;

    private String name;

    private String password;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Timeline> timeline;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Comment> commentList;

}
