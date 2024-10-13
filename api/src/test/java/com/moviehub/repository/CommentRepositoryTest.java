package com.moviehub.repository;

import com.moviehub.TestcontainersConfiguration;
import com.moviehub.entity.Comment;
import com.moviehub.entity.Comment_;
import com.moviehub.entity.Movie;
import com.moviehub.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    private UserRepository userRepository;

    private Comment comment1;
    private Comment comment2;
    private Comment comment3;
    private User user;

    @BeforeEach
    void setUp() {
        Movie movie = createMovie();
        user = createUser("auth0|1", "John Doe", "john@example.com");
        User user2 = createUser("auth0|2", "James Bond", "james@example.com");

        comment1 = createComment(movie, user, "First comment", null);
        comment2 = createComment(movie, user, "Second comment", comment1);
        comment3 = createComment(movie, user2, "Third comment", comment1);
    }

    @Test
    void shouldConnectToPostgres() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldFindRepliesForCommentsSortedByCreatedAtAsc() {
        List<UUID> commentIds = List.of(comment1.getId());
        Sort sort = Sort.by(Comment_.CREATED_AT)
                        .ascending();

        List<Comment> replies = commentRepository.findRepliesForComments(commentIds, sort);

        assertThat(replies).hasSize(2);
        assertThat(replies.getFirst()
                          .getText()).isEqualTo("Second comment");
        assertThat(replies.getLast()
                          .getText()).isEqualTo("Third comment");
    }

    @Test
    void shouldFindRepliesForCommentsSortedByCreatedAtDesc() {
        List<UUID> commentIds = List.of(comment1.getId());
        Sort sort = Sort.by(Comment_.CREATED_AT)
                        .descending();

        List<Comment> replies = commentRepository.findRepliesForComments(commentIds, sort);

        assertThat(replies).hasSize(2);
        assertThat(replies.getFirst()
                          .getText()).isEqualTo("Third comment");
        assertThat(replies.getLast()
                          .getText()).isEqualTo("Second comment");
    }

    @Test
    void shouldReturnEmptyRepliesForCommentsWhenCommentHasNoReplies() {
        List<UUID> commentIds = List.of(comment2.getId());
        Sort sort = Sort.by(Comment_.CREATED_AT)
                        .descending();

        List<Comment> replies = commentRepository.findRepliesForComments(commentIds, sort);

        assertThat(replies).isEmpty();
    }

    @Test
    void shouldReturnEmptyRepliesForCommentsWhenCommentIdsAreEmpty() {
        List<UUID> commentIds = Collections.emptyList();
        Sort sort = Sort.by(Comment_.CREATED_AT)
                        .descending();

        List<Comment> replies = commentRepository.findRepliesForComments(commentIds, sort);

        assertThat(replies).isEmpty();
    }

    @Test
    void shouldFilterCommentsPostedByUser() {
        List<UUID> commentIds = Arrays.asList(comment1.getId(), comment2.getId(), comment3.getId());

        List<UUID> filteredCommentIds = commentRepository.filterCommentsPostedByUser(commentIds, user);

        assertThat(filteredCommentIds).containsExactlyInAnyOrder(comment1.getId(), comment2.getId());
    }

    @Test
    void shouldReturnEmptyFilterCommentsPostedByUserWhenUserDoesNotExists() {
        List<UUID> commentIds = Arrays.asList(comment1.getId(), comment2.getId(), comment3.getId());

        List<UUID> filteredCommentIds = commentRepository.filterCommentsPostedByUser(commentIds, null);

        assertThat(filteredCommentIds).isEmpty();
    }

    @Test
    void shouldReturnEmptyFilterCommentsPostedByUserWhenCommentIdsAreEmpty() {
        List<UUID> commentIds = Collections.emptyList();

        List<UUID> filteredCommentIds = commentRepository.filterCommentsPostedByUser(commentIds, user);

        assertThat(filteredCommentIds).isEmpty();
    }

    @Test
    void shouldFindAllTopLevelComments() {
        Movie movie = comment1.getMovie();
        Pageable pageable = Pageable.ofSize(10);

        Page<Comment> page = commentRepository.findAllTopLevelComments(movie, pageable);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent()
                       .getFirst()
                       .getText()).isEqualTo("First comment");
    }

    @Test
    void shouldReturnEmptyTopLevelCommentsWhenMovieDoesNotExist() {
        Pageable pageable = Pageable.ofSize(10);

        Page<Comment> page = commentRepository.findAllTopLevelComments(null, pageable);

        assertThat(page.getTotalElements()).isZero();
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

    private Comment createComment(Movie movie, User user, String text, Comment parentComment) {
        return commentRepository.save(Comment.builder()
                                             .movie(movie)
                                             .user(user)
                                             .text(text)
                                             .parentComment(parentComment)
                                             .build());
    }

}