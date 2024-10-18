import type { FC } from "react";
import type { components } from "@/api/api";
import { ReplyHeader } from "@/components/comment/reply/ReplyHeader";
import { ReplyBody } from "@/components/comment/reply/ReplyBody";
import { ReplyFooter } from "@/components/comment/reply/ReplyFooter";

interface ReplyProps {
  reply: components["schemas"]["CommentInfoDetailsResponse"];
  commentId: components["schemas"]["CommentInfoDetailsResponse"]["id"];
}

export const Reply: FC<ReplyProps> = ({ reply, commentId }) => {
  const { author, text, isDeleted } = reply;

  return (
    <div>
      <div className="mt-6 flex flex-row gap-4">
        <img src={author.pictureUrl} alt={author.name} className="mt-1 h-[52px] w-[52px] rounded-full" />
        <div className="flex w-full flex-col gap-1.5">
          <ReplyHeader reply={reply} />
          <ReplyBody text={text} isDeleted={isDeleted} />
          <ReplyFooter reply={reply} commentId={commentId} />
        </div>
      </div>
    </div>
  );
};
