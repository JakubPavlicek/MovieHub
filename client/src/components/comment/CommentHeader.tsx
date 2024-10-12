import type { components } from "@/api/api";
import type { FC } from "react";
import { formatDate } from "@/utils/commentDetails";
import { X } from "lucide-react";
import { useApi } from "@/context/ApiProvider";
import { useQueryClient } from "@tanstack/react-query";

interface CommentHeaderProps {
  comment: components["schemas"]["CommentDetailsResponse"];
}

export const CommentHeader: FC<CommentHeaderProps> = ({ comment }) => {
  const { id, isAuthor, author, isDeleted, createdAt } = comment;
  const formattedDate = formatDate(createdAt);

  const queryClient = useQueryClient();
  const api = useApi();
  const { mutate } = api.useMutation("delete", "/comments/{commentId}", {
    onSuccess: async () => queryClient.invalidateQueries({ queryKey: ["get", "/movies/{movieId}/comments"] }),
  });

  const deleteComment = () => {
    mutate({
      params: {
        path: { commentId: id },
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
        <button className="rounded-full p-1 text-red-400 hover:bg-gray-800" onClick={deleteComment}>
          <X />
        </button>
      )}
    </div>
  );
};
