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
    private List<Timeline> timeline;

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList;

    public static boolean isValidAddress(String email) {
        String validAddress = "dsm.hs.kr";
        String emailAddress = email.substring(email.indexOf("@") + 1);
        return validAddress.equals(emailAddress);
    }

}
