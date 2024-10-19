import type { components } from "@/api/api";
import { type FC } from "react";
import { CommentHeader } from "@/components/comment/CommentHeader";
import { CommentText } from "@/components/comment/CommentText";
import { CommentFooter } from "@/components/comment/CommentFooter";
import { useApi } from "@/context/ApiProvider";
import { CommentReply } from "@/components/comment/CommentReply";

interface CommentProps {
  comment: components["schemas"]["CommentInfoDetailsResponse"];
}

export const Comment: FC<CommentProps> = ({ comment }) => {
  const { id, author, text, isDeleted } = comment;

  const api = useApi();
  const { data: replies } = api.useQuery("get", "/comments/{commentId}/replies", {
    params: {
      path: { commentId: id },
    },
  });

  return (
    <div>
      <div className="mt-6 flex flex-row gap-4">
        <img src={author.pictureUrl} alt={author.name} className="mt-1 h-[52px] w-[52px] rounded-full" />
        <div className="flex w-full flex-col gap-1.5">
          <CommentHeader item={comment} />
          <CommentText text={text} isDeleted={isDeleted} />
          <CommentFooter item={comment} commentId={id} />
          {replies?.content.map((reply) => <CommentReply key={reply.id} reply={reply} commentId={id} />)}
        </div>
      </div>
    </div>
  );
};
