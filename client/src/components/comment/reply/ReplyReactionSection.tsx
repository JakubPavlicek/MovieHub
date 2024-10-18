import type { FC } from "react";
import { useTranslation } from "react-i18next";
import { useAuth0 } from "@auth0/auth0-react";
import { useQueryClient } from "@tanstack/react-query";
import { useApi } from "@/context/ApiProvider";
import type { components } from "@/api/api";
import { toast } from "react-toastify";
import { ThumbsDown, ThumbsUp } from "lucide-react";

interface ReplyReactionSectionProps {
  reply: components["schemas"]["CommentInfoDetailsResponse"];
}

export const ReplyReactionSection: FC<ReplyReactionSectionProps> = ({ reply }) => {
  const { t } = useTranslation();
  const { isAuthenticated } = useAuth0();
  const queryClient = useQueryClient();
  const api = useApi();
  const { mutate } = api.useMutation("post", "/replies/{replyId}/reactions", {
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["get", "/comments/{commentId}/replies"] }),
  });

  const submitReaction = (reaction: components["schemas"]["Reaction"]) => {
    if (!isAuthenticated) {
      toast.error(t("toast.unauthenticated"));
      return;
    }

    // user wants to remove the reaction
    if (reply.userReaction === reaction) {
      reaction = "none";
    }

    mutate({
      params: {
        path: { replyId: reply.id },
      },
      body: {
        reactionType: reaction,
      },
    });
  };

  return (
    <div className="flex flex-row gap-4">
      <div className="flex flex-row items-center gap-1 text-neutral-300">
        <span>{reply.likes}</span>
        <button
          className={`p-1 hover:text-cyan-300 ${reply.userReaction === "like" && "text-lime-400"}`}
          onClick={() => submitReaction("like")}
        >
          <ThumbsUp size={20} strokeWidth={2} />
        </button>
      </div>
      <div className="flex flex-row items-center gap-1 text-neutral-300">
        <span>{reply.dislikes}</span>
        <button
          className={`p-1 hover:text-cyan-300 ${reply.userReaction === "dislike" && "text-red-400"}`}
          onClick={() => submitReaction("dislike")}
        >
          <ThumbsDown size={20} strokeWidth={2} />
        </button>
      </div>
    </div>
  );
};
