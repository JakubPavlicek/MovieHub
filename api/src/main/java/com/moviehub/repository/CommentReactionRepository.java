package com.moviehub.repository;

import com.moviehub.entity.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, UUID> {

//    @Query("""
//           SELECT cr FROM CommentReaction cr
//           LEFT JOIN FETCH cr.comment c
//           LEFT JOIN FETCH cr.user u
//           WHERE cr.comment.id = :commentId AND cr.user.id = :userId
//           """)
//    Optional<CommentReaction> findByCommentIdAndUserId(UUID commentId, String userId);
//
//    void removeByComment_IdAndUser_Id(UUID commentId, String userId);

}
