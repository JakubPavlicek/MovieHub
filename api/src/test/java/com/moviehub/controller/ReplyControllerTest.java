package com.moviehub.controller;

import com.moviehub.config.SecurityConfig;
import com.moviehub.entity.ReactionType;
import com.moviehub.exception.ReplyNotFoundException;
import com.moviehub.service.CommentInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReplyController.class)
@Import(SecurityConfig.class)
class ReplyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentInfoService commentInfoService;

    private static final UUID REPLY_ID = UUID.fromString("9280d11b-995a-4cfd-8f2e-0d4d0fa0fbad");

    @ParameterizedTest
    @WithMockUser
    @ValueSource(
        strings = {
            "like",
            "dislike",
            "none"
        }
    )
    void shouldAddReplyReaction(String reaction) throws Exception {
        String content = """
                         {
                           "reactionType": "%s"
                         }
                         """.formatted(reaction);

        mvc.perform(post("/replies/{replyId}/reactions", REPLY_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content(content))
           .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldThrowReplyNotFoundExceptionWhenReplyDoesNotExists() throws Exception {
        doThrow(ReplyNotFoundException.class).when(commentInfoService)
                                             .addReplyReaction(REPLY_ID, ReactionType.LIKE);

        mvc.perform(post("/replies/{replyId}/reactions", REPLY_ID)
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
    void shouldNotAddReplyReactionWhenReactionIsInvalid() throws Exception {
        mvc.perform(post("/replies/{replyId}/reactions", REPLY_ID)
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
    void shouldNotAddReplyReactionWhenUserHasNoUserRole() throws Exception {
        mvc.perform(post("/replies/{replyId}/reactions", REPLY_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "reactionType": "like"
                        }
                        """))
           .andExpect(status().isForbidden());
    }

    @Test
    void shouldNotAddReplyReactionWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(post("/replies/{replyId}/reactions", REPLY_ID)
               .contentType(MediaType.APPLICATION_JSON)
               .content("""
                        {
                          "reactionType": "like"
                        }
                        """))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldDeleteReply() throws Exception {
        mvc.perform(delete("/replies/{replyId}", REPLY_ID))
           .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldThrowReplyNotFoundExceptionWhenReplyDoesNotExist() throws Exception {
        doThrow(ReplyNotFoundException.class).when(commentInfoService)
                                             .deleteReply(REPLY_ID);

        mvc.perform(delete("/replies/{replyId}", REPLY_ID))
           .andExpectAll(
               status().isNotFound(),
               content().contentType(MediaType.APPLICATION_PROBLEM_JSON)
           );
    }

    @Test
    @WithMockUser(roles = "ANONYMOUS")
    void shouldNotDeleteReplyWhenUserHasNoUserRole() throws Exception {
        mvc.perform(delete("/replies/{replyId}", REPLY_ID))
           .andExpect(status().isForbidden());
    }

    @Test
    void shouldNotDeleteReplyWhenUserIsNotAuthenticated() throws Exception {
        mvc.perform(delete("/replies/{replyId}", REPLY_ID))
           .andExpect(status().isUnauthorized());
    }

}