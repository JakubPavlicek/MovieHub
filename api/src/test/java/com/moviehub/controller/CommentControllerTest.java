package com.moviehub.controller;

import com.moviehub.config.SecurityConfig;
import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentReply;
import com.moviehub.entity.ReactionType;
import com.moviehub.entity.User;
import com.moviehub.exception.CommentNotFoundException;
import com.moviehub.service.CommentInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.moviehub.EntityBuilder.createComment;
import static com.moviehub.EntityBuilder.createReply;
import static com.moviehub.EntityBuilder.createUser;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@Import(SecurityConfig.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentInfoService commentInfoService;

    private static final UUID COMMENT_ID = UUID.fromString("e3a00624-0c5b-4416-95c1-a9e3daf3d970");

    @Test
    @WithMockUser
    void shouldDeleteComment() throws Exception {
        mvc.perform(delete("/comments/{commentId}", COMMENT_ID))
           .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldThrowCommentNotFoundExceptionWhenCommentDoesNotExist() throws Exception {
        doThrow(CommentNotFoundException.class).when(commentInfoService)
                                               .deleteComment(COMMENT_ID);

        mvc.perform(delete("/comments/{commentId}", COMMENT_ID))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser(roles = "ANONYMOUS")
    void shouldNotDeleteCommentWhenUserHasNoUserRole() throws Exception {
        mvc.perform(delete("/comments/{commentId}", COMMENT_ID))
           .andExpect(status().isForbidden());
    }

    @Test
    void shouldNotDeleteCommentWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(delete("/comments/{commentId}", COMMENT_ID))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldAddReply() throws Exception {
        mvc.perform(post("/comments/{commentId}/replies", COMMENT_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "text": "comment"
                        }
                        """))
           .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void shouldNotAddReplyWhenCommentDoesNotExist() throws Exception {
        doThrow(CommentNotFoundException.class).when(commentInfoService)
                                               .addReply(COMMENT_ID, "comment");

        mvc.perform(post("/comments/{commentId}/replies", COMMENT_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "text": "comment"
                        }
                        """))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    void shouldNotAddReplyWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(post("/comments/{commentId}/replies", COMMENT_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "text": "comment"
                        }
                        """))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ANONYMOUS")
    void shouldNotAddReplyWhenUserHasNoUserRole() throws Exception {
        mvc.perform(post("/comments/{commentId}/replies", COMMENT_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "text": "comment"
                        }
                        """))
           .andExpect(status().isForbidden());
    }

    @Test
    void shouldGetReplies() throws Exception {
        User user = createUser("James");
        Comment comment = (Comment) createComment(null, user, "comment");
        CommentReply reply = (CommentReply) createReply(comment, user, "reply");
        Page<CommentReply> replies = new PageImpl<>(List.of(reply), PageRequest.of(0, 5), 1L);

        when(commentInfoService.getReplies(COMMENT_ID, 0, 5)).thenReturn(replies);

        mvc.perform(get("/comments/{commentId}/replies", COMMENT_ID)
               .param("page", "0")
               .param("limit", "5"))
           .andExpectAll(
               status().isOk(),
               content().contentType(MediaType.APPLICATION_JSON),
               jsonPath("$.content").exists(),
               jsonPath("$.content[0].text").value("reply"),
               jsonPath("$.pageable.pageNumber").value(0),
               jsonPath("$.pageable.pageSize").value(5),
               jsonPath("$.numberOfElements").value(1)
           );
    }

    @Test
    void shouldNotGetRepliesWhenParamsAreInvalid() throws Exception {
        mvc.perform(get("/comments/{commentId}/replies", COMMENT_ID)
               .param("page", "-1")
               .param("limit", "6"))
           .andExpectAll(
               status().isBadRequest(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
               jsonPath("$.contextInfo.page").exists(),
               jsonPath("$.contextInfo.limit").exists()
           );
    }

    @Test
    void shouldNotGetRepliesWhenCommentDoesNotExist() throws Exception {
        doThrow(CommentNotFoundException.class).when(commentInfoService)
                                               .getReplies(COMMENT_ID, 0, 5);

        mvc.perform(get("/comments/{commentId}/replies", COMMENT_ID)
               .param("page", "0")
               .param("limit", "5"))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @ParameterizedTest
    @WithMockUser
    @ValueSource(
        strings = {
            "like",
            "dislike",
            "none"
        }
    )
    void shouldAddCommentReaction(String reaction) throws Exception {
        String content = """
                         {
                           "reactionType": "%s"
                         }
                         """.formatted(reaction);

        mvc.perform(post("/comments/{commentId}/reactions", COMMENT_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content(content))
           .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldThrowCommentNotFoundExceptionWhenCommentDoesNotExists() throws Exception {
        doThrow(CommentNotFoundException.class).when(commentInfoService)
                                               .addCommentReaction(COMMENT_ID, ReactionType.LIKE);

        mvc.perform(post("/comments/{commentId}/reactions", COMMENT_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "reactionType": "like"
                        }
                        """))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser
    void shouldNotAddCommentReactionWhenReactionIsInvalid() throws Exception {
        mvc.perform(post("/comments/{commentId}/reactions", COMMENT_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                           "reactionType": "invalid"
                        }
                        """))
           .andExpectAll(
               status().isBadRequest(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser(roles = "ANONYMOUS")
    void shouldNotAddCommentReactionWhenUserHasNoUserRole() throws Exception {
        mvc.perform(post("/replies/{replyId}/reactions", COMMENT_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "reactionType": "like"
                        }
                        """))
           .andExpect(status().isForbidden());
    }

    @Test
    void shouldNotAddCommentReactionWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(post("/comments/{commentId}/reactions", COMMENT_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "reactionType": "like"
                        }
                        """))
           .andExpect(status().isUnauthorized());
    }

}