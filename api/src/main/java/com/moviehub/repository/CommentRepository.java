package com.moviehub.repository;

import com.moviehub.entity.Comment;
import com.moviehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

//    @Query("SELECT c FROM Comment c WHERE c.parentComment.id IN :commentIds")
//    List<Comment> findRepliesForComments(List<UUID> commentIds, Sort sort);

    @Query("SELECT c.id FROM Comment c WHERE c.user = :user AND c.id IN :commentIds")
    List<UUID> filterCommentsPostedByUser(List<UUID> commentIds, User user);

//    @Query("SELECT c FROM Comment c WHERE c.movie.id = :movieId AND c.parentComment IS NULL")
//    Page<Comment> findAllTopLevelComments(UUID movieId, Pageable pageable);

}
