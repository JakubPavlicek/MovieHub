package com.moviehub.repository;

import com.moviehub.entity.Comment;
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
/// Repository interface for managing Comment entities.
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @Query(
        """
        SELECT c FROM Comment c
        JOIN FETCH c.user
        WHERE c.movie.id = :movieId
        """
    )
    Page<Comment> findCommentsByMovieId(UUID movieId, Pageable pageable);

    Optional<Comment> findByIdAndUser(UUID commentId, User user);

}
