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

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Entity class representing a comment reply.
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentReply extends CommentInfo {

    /// The comment to which this reply belongs.
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
