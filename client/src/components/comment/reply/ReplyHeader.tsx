import type { FC } from "react";
import { formatDate } from "@/utils/commentDetails";
import { useQueryClient } from "@tanstack/react-query";
import { useApi } from "@/context/ApiProvider";
import { X } from "lucide-react";
import type { components } from "@/api/api";

interface ReplyHeaderProps {
  reply: components["schemas"]["CommentInfoDetailsResponse"];
}

export const ReplyHeader: FC<ReplyHeaderProps> = ({ reply }) => {
  const { id, isAuthor, author, isDeleted, createdAt } = reply;
  const formattedDate = formatDate(createdAt);

  const queryClient = useQueryClient();
  const api = useApi();
  const { mutate } = api.useMutation("delete", "/replies/{replyId}", {
    onSuccess: async () => queryClient.invalidateQueries({ queryKey: ["get", "/comments/{commentId}/replies"] }),
  });

  const deleteComment = () => {
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
        <button className="rounded-full p-1 text-red-400 hover:bg-gray-800" onClick={deleteComment}>
          <X />
        </button>
      )}
    </div>
  );
};
