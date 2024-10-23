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

    /// Unique identifier for the comment/reply.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /// The user who created the comment/reply.
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

    /// The timestamp when the comment/reply was created.
    @CreationTimestamp
    @Column(
        updatable = false,
        nullable = false
    )
    private LocalDateTime createdAt;

    /// The content of the comment/reply.
    @Column(
        nullable = false
    )
    private String text;

    /// Indicates whether the comment/reply is deleted.
    @Column(
        nullable = false
    )
    private Boolean isDeleted = false;

    /// The number of likes for the comment/reply.
    @Column(
        nullable = false
    )
    Long likes = 0L;

    /// The number of dislikes for the comment/reply.
    @Column(
        nullable = false
    )
    Long dislikes = 0L;

    /// List of reactions to the comment/reply.
    @OneToMany(
        mappedBy = "commentInfo",
        fetch = FetchType.LAZY
    )
    List<CommentReaction> reactions = new ArrayList<>();

    /// The user's reaction type to the comment/reply.
    @Transient
    private ReactionType userReaction = ReactionType.NONE;

    /// Indicates if the user is the author of the comment/reply.
    @Transient
    private boolean isAuthor = false;

    /// Sets the user's reaction based on the list of reactions.
    ///
    /// @param reactions the list of reactions to process
    public void setUserReaction(List<CommentReaction> reactions) {
        userReaction = reactions.stream()
                                .filter(r -> r.getCommentInfo()
                                              .getId()
                                              .equals(id))
                                .map(CommentReaction::getReactionType)
                                .findFirst()
                                .orElse(ReactionType.NONE);
    }

    /// Sets the author status for the current user.
    ///
    /// @param currentUser the user to compare with the comment's/reply's author
    public void setIsAuthor(User currentUser) {
        isAuthor = user.getId().equals(currentUser.getId());
    }

}
