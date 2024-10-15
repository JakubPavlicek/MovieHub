package com.moviehub.repository;

import com.moviehub.TestcontainersConfiguration;
import com.moviehub.entity.Comment;
import com.moviehub.entity.Director;
import com.moviehub.entity.Movie;
import com.moviehub.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private MovieRepository movieRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = createMovie();
        user = createUser("auth0|1", "John Doe", "john@example.com");
        User user2 = createUser("auth0|2", "James Bond", "james@example.com");

        createComment(movie, user, "First comment");
        createComment(movie, user, "Second comment");
        createComment(movie, user2, "Third comment");
    }

    @Test
    void shouldConnectToPostgres() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldFilterCommentsPostedByUser() {
        List<UUID> commentIds = commentRepository.findAll()
                                                 .stream()
                                                 .map(Comment::getId)
                                                 .toList();

        List<UUID> filteredComments = commentRepository.filterCommentsPostedByUser(commentIds, user);

        assertThat(filteredComments).hasSize(2); // Expecting 2 comments by the user
    }

    @Test
    void shouldFindAllCommentsByMovieId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentsPage = commentRepository.findAllComments(movie.getId(), pageable);

        assertThat(commentsPage).isNotNull();
        assertThat(commentsPage.getContent()).hasSize(3); // Expecting 3 comments total
    }

    @Test
    void shouldFindCommentsByMovieId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentsPage = commentRepository.findCommentsByMovieId(movie.getId(), pageable);

        assertThat(commentsPage).isNotNull();
        assertThat(commentsPage.getContent()).hasSize(3); // Expecting 3 comments total
    }

    @Test
    void shouldFindCommentByIdAndUser() {
        UUID commentId = commentRepository.findAll()
                                          .getFirst()
                                          .getId();
        Optional<Comment> foundComment = commentRepository.findByIdAndUser(commentId, user);

        assertThat(foundComment).isPresent();
        assertThat(foundComment.get()
                               .getUser()).isEqualTo(user);
    }

    private Movie createMovie() {
        return movieRepository.save(Movie.builder()
                                         .name("Movie")
                                         .filename("movie.mp4")
                                         .releaseDate(LocalDate.parse("2024-10-13"))
                                         .duration(120)
                                         .description("A test movie")
                                         .posterUrl("https://example.com/poster.jpg")
                                         .trailerUrl("https://example.com/trailer.mp4")
                                         .director(createDirector())
                                         .build());
    }

    private Director createDirector() {
        return directorRepository.save(Director.builder()
                                               .name("Director")
                                               .build());
    }

    private User createUser(String id, String name, String email) {
        return userRepository.save(User.builder()
                                       .id(id)
                                       .name(name)
                                       .email(email)
                                       .pictureUrl("https://example.com/pic.jpg")
                                       .build());
    }

    private void createComment(Movie movie, User user, String text) {
        Comment comment = new Comment();
        comment.setMovie(movie);
        comment.setUser(user);
        comment.setText(text);

        commentRepository.save(comment);
    }

}