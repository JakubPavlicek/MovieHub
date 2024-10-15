package com.moviehub.repository;

import com.moviehub.entity.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentReplyRepository extends JpaRepository<CommentReply, UUID> {

}
