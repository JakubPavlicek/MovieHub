package com.moviehub.repository;

import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    Page<Comment> findAllByMovie(Movie movie, Pageable pageable);

}
