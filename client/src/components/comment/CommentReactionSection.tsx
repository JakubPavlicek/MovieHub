import type { components } from "@/api/api";
import type { FC } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import { useQueryClient } from "@tanstack/react-query";
import { useApi } from "@/context/ApiProvider";
import { toast } from "react-toastify";
import { ThumbsDown, ThumbsUp } from "lucide-react";

interface CommentReactionSection {
  comment: components["schemas"]["CommentDetailsResponse"];
}

export const CommentReactionSection: FC<CommentReactionSection> = ({ comment }) => {
  const { isAuthenticated } = useAuth0();
  const queryClient = useQueryClient();
  const api = useApi();
  const { mutate } = api.useMutation("post", "/comments/{commentId}/reactions", {
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["get", "/movies/{movieId}/comments"] }),
  });

  const submitReaction = (reaction: components["schemas"]["Reaction"]) => {
    if (!isAuthenticated) {
      toast.error(import.meta.env.VITE_NOT_AUTHENTICATED_MESSAGE);
      return;
    }

    mutate({
      params: {
        path: { commentId: comment.id },
      },
      body: {
        reactionType: reaction,
      },
    });
  };

  return (
    <div className="flex flex-row gap-4">
      <div className="flex flex-row items-center gap-1 text-neutral-300">
        <span>{comment.likes}</span>
        <button className="p-1 hover:text-cyan-300" onClick={() => submitReaction("like")}>
          <ThumbsUp size={20} strokeWidth={2} />
        </button>
      </div>
      <div className="flex flex-row items-center gap-1 text-neutral-300">
        <span>{comment.dislikes}</span>
        <button className="p-1 hover:text-cyan-300" onClick={() => submitReaction("dislike")}>
          <ThumbsDown size={20} strokeWidth={2} />
        </button>
      </div>
    </div>
  );
};