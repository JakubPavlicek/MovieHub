package com.moviehub;

import com.moviehub.entity.Comment;
import com.moviehub.entity.Movie;
import com.moviehub.service.CommentService;
import com.moviehub.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class FetchTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private CommentService commentService;

    @Test
    void name() {
        Movie movie = movieService.getMovie(UUID.fromString("8c328172-a60f-4ea6-93b1-ca2a5cca2bf0"));

        Page<Comment> comments = commentService.getComments(movie, PageRequest.of(0, 10));
    }

}
