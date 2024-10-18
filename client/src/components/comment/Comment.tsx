import type { components } from "@/api/api";
import { type FC } from "react";
import { CommentHeader } from "@/components/comment/CommentHeader";
import { CommentBody } from "@/components/comment/CommentBody";
import { CommentFooter } from "@/components/comment/CommentFooter";
import { useApi } from "@/context/ApiProvider";
import { Reply } from "@/components/comment/reply/Reply";

interface CommentProps {
  comment: components["schemas"]["CommentInfoDetailsResponse"];
  movieId: components["schemas"]["MovieDetailsResponse"]["id"];
}

export const Comment: FC<CommentProps> = ({ comment, movieId }) => {
  const { id, author, text, isDeleted } = comment;

  const api = useApi();
  const { data: replies } = api.useQuery("get", "/comments/{commentId}/replies", {
    params: {
      path: {
        commentId: id,
      },
    },
  });

  return (
    <div>
      <div className="mt-6 flex flex-row gap-4">
        <img src={author.pictureUrl} alt={author.name} className="mt-1 h-[52px] w-[52px] rounded-full" />
        <div className="flex w-full flex-col gap-1.5">
          <CommentHeader comment={comment} />
          <CommentBody text={text} isDeleted={isDeleted} />
          <CommentFooter comment={comment} movieId={movieId} />
          {replies?.content.map((reply) => <Reply key={reply.id} reply={reply} commentId={id} />)}
        </div>
      </div>
    </div>
  );
};
