package com.moviehub.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Entity class representing a comment.
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment extends CommentInfo {

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "movie_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_movie"),
        nullable = false
    )
    private Movie movie;

    @OneToMany(
        mappedBy = "comment",
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    @OrderBy("createdAt ASC")
    @Builder.Default
    private List<CommentReply> replies = new ArrayList<>();

}
