package com.moviehub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Entity class representing a comment/reply info.
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class CommentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_user"),
        nullable = false
    )
    private User user;

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
        nullable = false
    )
    Long likes = 0L;

    @Column(
        nullable = false
    )
    Long dislikes = 0L;

    @OneToMany(
        mappedBy = "commentInfo",
        fetch = FetchType.LAZY
    )
    List<CommentReaction> reactions = new ArrayList<>();

    @Transient
    private ReactionType userReaction = ReactionType.NONE;

    @Transient
    private boolean isAuthor = false;

    public void setUserReaction(List<CommentReaction> reactions) {
        userReaction = reactions.stream()
                                .filter(r -> r.getCommentInfo()
                                              .getId()
                                              .equals(id))
                                .map(CommentReaction::getReactionType)
                                .findFirst()
                                .orElse(ReactionType.NONE);
    }

    public void setIsAuthor(User currentUser) {
        isAuthor = user.getId().equals(currentUser.getId());
    }

}
