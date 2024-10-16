package com.moviehub.repository;

import com.moviehub.TestcontainersConfiguration;
import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentInfo;
import com.moviehub.entity.Director;
import com.moviehub.entity.Movie;
import com.moviehub.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static com.moviehub.EntityBuilder.createComment;
import static com.moviehub.EntityBuilder.createDirector;
import static com.moviehub.EntityBuilder.createMovie;
import static com.moviehub.EntityBuilder.createUser;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {"spring.liquibase.contexts=test"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class CommentRepositoryTest {

    @Autowired
    private PostgreSQLContainer<?> postgres;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private static User user;
    private static Movie movie;
    private static CommentInfo comment;

    @BeforeEach
    void setUp() {
        Director director = entityManager.persistAndFlush(createDirector("Director"));

        movie = createMovie("Movie");
        movie.setDirector(director);
        movie = entityManager.persistAndFlush(movie);

        user = entityManager.persistAndFlush(createUser("John"));
        User user2 = entityManager.persistAndFlush(createUser("James Bond"));

        comment = entityManager.persistAndFlush(createComment(movie, user, "First comment"));
        entityManager.persistAndFlush(createComment(movie, user, "Second comment"));
        entityManager.persistAndFlush(createComment(movie, user2, "Third comment"));
    }

    @Test
    void shouldConnectToPostgres() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldFindCommentsByMovieId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentsPage = commentRepository.findCommentsByMovieId(movie.getId(), pageable);

        assertThat(commentsPage).isNotNull();
        assertThat(commentsPage.getContent()).hasSize(3);
    }

    @Test
    void shouldFindCommentByIdAndUser() {
        Optional<Comment> foundComment = commentRepository.findByIdAndUser(comment.getId(), user);

        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getUser()).isEqualTo(user);
    }

}