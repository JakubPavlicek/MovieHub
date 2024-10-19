import type { components } from "@/api/api";
import type { FC } from "react";
import { formatDate } from "@/utils/commentDetails";
import { X } from "lucide-react";
import { useApi } from "@/context/ApiProvider";
import { useQueryClient } from "@tanstack/react-query";

interface CommentHeaderProps {
  item: components["schemas"]["CommentInfoDetailsResponse"];
  isReply?: boolean;
}

export const CommentHeader: FC<CommentHeaderProps> = ({ item, isReply = false }) => {
  const { id, isAuthor, author, isDeleted, createdAt } = item;
  const formattedDate = formatDate(createdAt);

  const queryClient = useQueryClient();
  const api = useApi();
  const { mutate } = api.useMutation("delete", isReply ? "/replies/{replyId}" : "/comments/{commentId}", {
    onSuccess: async () => {
      const queryKey = isReply ? ["get", "/comments/{commentId}/replies"] : ["get", "/movies/{movieId}/comments"];
      await queryClient.invalidateQueries({ queryKey });
    },
  });

  const deleteComment = () => {
    mutate({
      params: {
        path: { commentId: id },
      },
    });
  };

  const deleteReply = () => {
    mutate({
      params: {
        path: { replyId: id },
      },
    });
  };

  return (
    <div className="flex flex-row justify-between">
      <div className="flex flex-row items-center gap-3">
        <span className="font-semibold text-neutral-300">{author.name}</span>
        <span className="text-sm text-neutral-400">{formattedDate}</span>
      </div>
      {isAuthor && !isDeleted && (
        <button
          className="rounded-full p-1 text-red-400 hover:bg-gray-800"
          onClick={isReply ? deleteReply : deleteComment}
        >
          <X />
        </button>
      )}
    </div>
  );
};
