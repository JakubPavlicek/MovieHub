import type { FC } from "react";
import type { components } from "@/api/api";
import { CommentText } from "@/components/comment/CommentText";
import { CommentHeader } from "@/components/comment/CommentHeader";
import { CommentFooter } from "@/components/comment/CommentFooter";

interface CommentReplyProps {
  reply: components["schemas"]["CommentInfoDetailsResponse"];
  commentId: components["schemas"]["CommentInfoDetailsResponse"]["id"];
}

export const CommentReply: FC<CommentReplyProps> = ({ reply, commentId }) => {
  const { author, text, isDeleted } = reply;

  return (
    <div>
      <div className="mt-6 flex flex-row gap-4">
        <img src={author.pictureUrl} alt={author.name} className="mt-1 h-[52px] w-[52px] rounded-full" />
        <div className="flex w-full flex-col gap-1.5">
          <CommentHeader item={reply} isReply={true} />
          <CommentText text={text} isDeleted={isDeleted} />
          <CommentFooter item={reply} commentId={commentId} isReply={true} />
        </div>
      </div>
    </div>
  );
};
