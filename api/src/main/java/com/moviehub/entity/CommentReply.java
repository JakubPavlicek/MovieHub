package com.moviehub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentReply extends CommentInfo {

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "comment_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_comment"),
        nullable = false
    )
    private Comment comment;

}
