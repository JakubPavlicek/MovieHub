package com.moviehub.repository;

import com.moviehub.entity.CommentReply;
import com.moviehub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/// @author Jakub Pavlíček
/// @version 1.0
///
/// Repository interface for managing CommentReply entities.
@Repository
public interface CommentReplyRepository extends JpaRepository<CommentReply, UUID> {

    @Query(
        """
        SELECT cr FROM CommentReply cr
        JOIN FETCH cr.user
        WHERE cr.comment.id = :commentId
        """
    )
    Page<CommentReply> findAllReplies(UUID commentId, Pageable pageable);

    Optional<CommentReply> findByIdAndUser(UUID replyId, User user);

}
