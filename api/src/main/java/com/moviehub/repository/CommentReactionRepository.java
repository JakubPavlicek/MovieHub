package com.moviehub.repository;

import com.moviehub.entity.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Repository interface for managing CommentReaction entities.
@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, UUID> {

    @Query(
        """
        SELECT cr FROM CommentReaction cr
        JOIN FETCH cr.commentInfo ci
        JOIN FETCH cr.user u
        WHERE ci.id = :commentInfoId AND u.id = :userId
        """
    )
    Optional<CommentReaction> findByCommentInfoIdAndUserId(UUID commentInfoId, String userId);

    @Query(
        """
        SELECT cr FROM CommentReaction cr
        WHERE cr.user.id = :userId AND cr.commentInfo.id IN :commentIds
        """
    )
    List<CommentReaction> findUserReactionsByCommentIds(String userId, List<UUID> commentIds);

}
