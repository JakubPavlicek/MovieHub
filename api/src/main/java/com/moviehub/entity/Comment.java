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
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(
        nullable = true
    )
    private UUID parentCommentId;

    @OneToMany(
        mappedBy = "comment",
        fetch = FetchType.LAZY
    )
    List<CommentReaction> reactions;

}
