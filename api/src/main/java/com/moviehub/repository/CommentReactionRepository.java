package com.moviehub.repository;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, String> {

    boolean existsByCommentAndUserId(Comment comment, String userId);

    void removeByCommentAndUserId(Comment comment, String userId);

}
