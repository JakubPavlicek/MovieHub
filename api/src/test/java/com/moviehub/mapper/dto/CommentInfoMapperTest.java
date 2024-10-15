package com.moviehub.mapper.dto;

import com.moviehub.dto.CommentInfoDetailsResponse;
import com.moviehub.dto.CommentInfoPage;
import com.moviehub.entity.Comment;
import com.moviehub.entity.CommentInfo;
import com.moviehub.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CommentInfoMapperTest {

    private static final UUID FIRST_COMMENT_ID = UUID.randomUUID();
    private static final UUID SECOND_COMMENT_ID = UUID.randomUUID();

    private static final String FIRST_COMMENT_TEXT = "This is a comment.";
    private static final String SECOND_COMMENT_TEXT = "Another comment.";

    private static final Long LIKES = 10L;
    private static final Long DISLIKES = 5L;

    @Test
    void shouldMapToCommentInfoDetailsResponse() {
        CommentInfo commentInfo = createCommentInfo(FIRST_COMMENT_ID, FIRST_COMMENT_TEXT);

        CommentInfoDetailsResponse response = CommentInfoMapper.mapToCommentInfoDetailsResponse(commentInfo);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(FIRST_COMMENT_ID);
        assertThat(response.getText()).isEqualTo(FIRST_COMMENT_TEXT);
        assertThat(response.getIsDeleted()).isFalse();
        assertThat(response.getLikes()).isEqualTo(LIKES);
        assertThat(response.getDislikes()).isEqualTo(DISLIKES);
    }

    @Test
    void shouldMapToCommentInfoPage() {
        List<CommentInfo> commentInfos = List.of(
            createCommentInfo(FIRST_COMMENT_ID, FIRST_COMMENT_TEXT),
            createCommentInfo(SECOND_COMMENT_ID, SECOND_COMMENT_TEXT)
        );

        Page<CommentInfo> page = new PageImpl<>(commentInfos, Pageable.ofSize(5), commentInfos.size());

        CommentInfoPage responsePage = CommentInfoMapper.mapToCommentInfoPage(page);

        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getContent()).hasSize(commentInfos.size());
        assertThat(responsePage.getTotalElements()).isEqualTo(page.getTotalElements());
        assertThat(responsePage.getTotalPages()).isEqualTo(page.getTotalPages());
        assertThat(responsePage.getEmpty()).isFalse();
    }

    private static CommentInfo createCommentInfo(UUID id, String text) {
        CommentInfo commentInfo = new Comment();
        commentInfo.setId(id);
        commentInfo.setText(text);
        commentInfo.setCreatedAt(LocalDateTime.of(2024, 10, 10, 5, 4));
        commentInfo.setIsDeleted(false);
        commentInfo.setLikes(LIKES);
        commentInfo.setDislikes(DISLIKES);
        commentInfo.setUser(createUser());

        return commentInfo;
    }

    private static User createUser() {
        return User.builder()
                   .id("auth0|1")
                   .name("testUser")
                   .pictureUrl("https://example.com/picture.jpg")
                   .build();
    }

}