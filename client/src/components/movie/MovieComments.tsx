import { type FC, useState } from "react";
import type { components } from "@/api/api";
import { useApi } from "@/context/ApiProvider";
import { ThumbsDown, ThumbsUp } from "lucide-react";
import { CommentReplyInput } from "@/components/movie/CommentReplyInput";
import { useQueryClient } from "@tanstack/react-query";
import { toast } from "react-toastify";
import { useAuth0 } from "@auth0/auth0-react";

interface CommentReactionSection {
  comment: components["schemas"]["CommentDetailsResponse"];
}

const CommentReactionSection: FC<CommentReactionSection> = ({ comment }) => {
  const { isAuthenticated } = useAuth0();
  const queryClient = useQueryClient();
  const api = useApi();
  const { mutate } = api.useMutation("post", "/comments/{commentId}/reactions", {
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["get", "/movies/{movieId}/comments"] }),
  });

  const submitReaction = (reactionType: "like" | "dislike" | "none") => {
    if (!isAuthenticated) {
      toast.error(import.meta.env.VITE_NOT_AUTHENTICATED_MESSAGE);
      return;
    }

    mutate({
      params: {
        path: { commentId: comment.id },
      },
      body: {
        reactionType: reactionType,
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

interface MovieCommentProps {
  comment: components["schemas"]["CommentDetailsResponse"];
  topLevelCommentId: components["schemas"]["CommentDetailsResponse"]["id"];
}

const MovieComment: FC<MovieCommentProps> = ({ comment, topLevelCommentId }) => {
  const [showInput, setShowInput] = useState(false);

  const { user, createdAt, text } = comment;

  const formattedDate = new Intl.DateTimeFormat("en-US", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  })
    .format(new Date(createdAt))
    .replace(",", "");

  const regex = /@(\w+\s+)(.*)/;
  const match = regex.exec(text);

  const highlightedUser = match ? match[1] : "";
  const remainingText = match ? match[2] : text;

  return (
    <div>
      <div className="mt-6 flex flex-row gap-4">
        <img src={user?.pictureUrl} alt={user?.name} className="mt-1 h-[52px] w-[52px] rounded-full" />
        <div className="flex w-full flex-col gap-1.5">
          <div className="flex flex-row items-center gap-3">
            <span className="font-semibold">{user?.name}</span>
            <span className="text-sm text-neutral-400">{formattedDate}</span>
          </div>
          <div className="text-neutral-200">
            {highlightedUser && <span className="text-cyan-300">@{highlightedUser}</span>}
            <span>{remainingText}</span>
          </div>
          <div className="flex flex-row items-center justify-between">
            <button
              className="max-w-fit text-neutral-400 hover:text-cyan-300"
              onClick={() => setShowInput((prev) => !prev)}
            >
              Reply
            </button>
            <CommentReactionSection comment={comment} />
          </div>
          {showInput && (
            <CommentReplyInput
              movieId={comment.movieId}
              parentCommentId={topLevelCommentId}
              replierUserName={comment.user.name}
              setShowInput={setShowInput}
            />
          )}
          {comment.replies?.map((reply) => (
            <MovieComment key={reply.id} comment={reply} topLevelCommentId={topLevelCommentId} />
          ))}
        </div>
      </div>
    </div>
  );
};

interface MovieCommentsProps {
  movieId: components["schemas"]["Uuid"];
}

export const MovieComments: FC<MovieCommentsProps> = ({ movieId }) => {
  const api = useApi();
  const { data: comments } = api.useQuery("get", "/movies/{movieId}/comments", {
    params: {
      path: { movieId: movieId },
      query: { limit: 5 },
    },
  });

  if (!comments?.content) {
    return <div className="text-white">Empty</div>;
  }

  return (
    <div className="mt-5 text-white">
      <span className="text-2xl font-semibold">Comments</span>
      {comments.content.map((comment) => (
        <MovieComment key={comment.id} comment={comment} topLevelCommentId={comment.id} />
      ))}
    </div>
  );
};
