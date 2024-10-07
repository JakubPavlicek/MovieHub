package com.moviehub.repository;

import com.moviehub.dto.UserCommentReaction;
import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReaction;
import com.moviehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, UUID> {

    Optional<CommentReaction> findByCommentAndUser(Comment comment, User user);

    void removeByCommentAndUser(Comment comment, User user);

    @Query("""
        SELECT new com.moviehub.dto.UserCommentReaction(cr.comment.id, cr.reactionType)
        FROM CommentReaction cr
        WHERE cr.comment.id IN :commentIds AND cr.user = :user
    """)
    List<UserCommentReaction> filterReactionsCreatedByUser(List<UUID> commentIds, User user);

}
