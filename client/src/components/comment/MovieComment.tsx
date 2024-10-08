import type { components } from "@/api/api";
import { type FC } from "react";
import { CommentHeader } from "@/components/comment/CommentHeader";
import { CommentBody } from "@/components/comment/CommentBody";
import { CommentFooter } from "@/components/comment/CommentFooter";

interface MovieCommentProps {
  comment: components["schemas"]["CommentDetailsResponse"];
  topLevelCommentId: components["schemas"]["CommentDetailsResponse"]["id"];
}

export const MovieComment: FC<MovieCommentProps> = ({ comment, topLevelCommentId }) => {
  const { author, text, isDeleted, replies } = comment;

  return (
    <div>
      <div className="mt-6 flex flex-row gap-4">
        <img src={author.pictureUrl} alt={author.name} className="mt-1 h-[52px] w-[52px] rounded-full" />
        <div className="flex w-full flex-col gap-1.5">
          <CommentHeader comment={comment} />
          <CommentBody text={text} isDeleted={isDeleted} />
          <CommentFooter comment={comment} topLevelCommentId={topLevelCommentId} />
          {replies.map((reply) => (
            <MovieComment key={reply.id} comment={reply} topLevelCommentId={topLevelCommentId} />
          ))}
        </div>
      </div>
    </div>
  );
};
