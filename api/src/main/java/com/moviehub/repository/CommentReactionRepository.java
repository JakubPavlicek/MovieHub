package com.moviehub.repository;

import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReaction;
import com.moviehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, UUID> {

    Optional<CommentReaction> findByCommentAndUser(Comment comment, User user);

    void removeByCommentAndUser(Comment comment, User user);

}
