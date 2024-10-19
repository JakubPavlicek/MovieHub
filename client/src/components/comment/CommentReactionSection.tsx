import type { components } from "@/api/api";
import type { FC } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import { useQueryClient } from "@tanstack/react-query";
import { useApi } from "@/context/ApiProvider";
import { toast } from "react-toastify";
import { ThumbsDown, ThumbsUp } from "lucide-react";
import { useTranslation } from "react-i18next";

interface CommentReactionSectionProps {
  item: components["schemas"]["CommentInfoDetailsResponse"];
  isReply?: boolean;
}

export const CommentReactionSection: FC<CommentReactionSectionProps> = ({ item, isReply = false }) => {
  const { t } = useTranslation();
  const { isAuthenticated } = useAuth0();
  const queryClient = useQueryClient();
  const api = useApi();

  const endpoint = isReply ? "/replies/{replyId}/reactions" : "/comments/{commentId}/reactions";
  const { mutate } = api.useMutation("post", endpoint, {
    onSuccess: async () => {
      const queryKey = isReply ? ["get", "/comments/{commentId}/replies"] : ["get", "/movies/{movieId}/comments"];
      await queryClient.invalidateQueries({ queryKey });
    },
  });

  const submitReaction = (reaction: components["schemas"]["Reaction"]) => {
    if (!isAuthenticated) {
      toast.error(t("toast.unauthenticated"));
      return;
    }

    // user wants to remove the reaction
    if (item.userReaction === reaction) {
      reaction = "none";
    }

    if (isReply) {
      submitReplyReaction(reaction);
    } else {
      submitCommentReaction(reaction);
    }
  };

  const submitCommentReaction = (reaction: components["schemas"]["Reaction"]) => {
    mutate({
      params: {
        path: { commentId: item.id },
      },
      body: {
        reactionType: reaction,
      },
    });
  };

  const submitReplyReaction = (reaction: components["schemas"]["Reaction"]) => {
    mutate({
      params: {
        path: { replyId: item.id },
      },
      body: {
        reactionType: reaction,
      },
    });
  };

  return (
    <div className="flex flex-row gap-4">
      <div className="flex flex-row items-center gap-1 text-neutral-300">
        <span>{item.likes}</span>
        <button
          className={`p-1 hover:text-cyan-300 ${item.userReaction === "like" && "text-lime-400"}`}
          onClick={() => submitReaction("like")}
        >
          <ThumbsUp size={20} strokeWidth={2} />
        </button>
      </div>
      <div className="flex flex-row items-center gap-1 text-neutral-300">
        <span>{item.dislikes}</span>
        <button
          className={`p-1 hover:text-cyan-300 ${item.userReaction === "dislike" && "text-red-400"}`}
          onClick={() => submitReaction("dislike")}
        >
          <ThumbsDown size={20} strokeWidth={2} />
        </button>
      </div>
    </div>
  );
};
