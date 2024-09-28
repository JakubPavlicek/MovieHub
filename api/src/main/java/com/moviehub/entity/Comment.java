package com.moviehub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(
        name = "movie_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_movie")
    )
    private Movie movie;

    @Column(
        nullable = false
    )
    private String userId;

    @CreationTimestamp
    @Column(
        updatable = false,
        nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
        nullable = false
    )
    private String text;

    @Column(
        nullable = false
    )
    private Boolean isDeleted = false;

    @Column(
        nullable = true
    )
    private String parentCommentId;

    @OneToMany(
        mappedBy = "comment",
        fetch = FetchType.LAZY
    )
    List<CommentReaction> reactions;

}