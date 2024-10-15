package com.moviehub.repository;

import com.moviehub.entity.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, UUID> {

    @Query("""
           SELECT cr FROM CommentReaction cr
           LEFT JOIN FETCH cr.commentInfo ci
           LEFT JOIN FETCH cr.user u
           WHERE cr.commentInfo.id = :commentInfoId AND cr.user.id = :userId
           """)
    Optional<CommentReaction> findByCommentInfoIdAndUserId(UUID commentInfoId, String userId);

    void removeByCommentInfo_IdAndUser_Id(UUID commentInfoId, String userId);

}
