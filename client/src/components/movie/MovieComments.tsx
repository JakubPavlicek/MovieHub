import React, { type FC, useState } from "react";
import type { components } from "@/api/api";
import { useApi } from "@/context/ApiProvider";
import { ThumbsDown, ThumbsUp } from "lucide-react";
import { CommentInput } from "@/components/movie/CommentInput";
import { ToastContainer } from "react-toastify";

interface CommentReactionSection {
  comment: components["schemas"]["CommentDetailsResponse"];
}

const CommentReactionSection: FC<CommentReactionSection> = ({ comment }) => {
  return (
    <div className="flex flex-row gap-4">
      <div className="flex flex-row items-center gap-1 text-neutral-300">
        <span>{comment.likes}</span>
        <button className="p-1 hover:text-cyan-300">
          <ThumbsUp size={20} strokeWidth={2} />
        </button>
      </div>
      <div className="flex flex-row items-center gap-1 text-neutral-300">
        <span>{comment.dislikes}</span>
        <button className="p-1 hover:text-cyan-300">
          <ThumbsDown size={20} strokeWidth={2} />
        </button>
      </div>
    </div>
  );
};

interface MovieCommentProps {
  comment: components["schemas"]["CommentDetailsResponse"];
}

const MovieComment: FC<MovieCommentProps> = ({ comment }) => {
  const [showInput, setShowInput] = useState(false);

  const { user, createdAt, text } = comment;

  const date = new Date(createdAt!);

  const formattedDate = new Intl.DateTimeFormat("en-US", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  })
    .format(date)
    .replace(",", "");

  return (
    <div>
      <div className="mt-6 flex flex-row gap-4">
        <img src={user?.pictureUrl} alt={user?.name} className="mt-1 h-[52px] w-[52px] rounded-full" />
        <div className="flex w-full flex-col gap-1.5">
          <div className="flex flex-row items-center gap-3">
            <span className="font-semibold">{user?.name}</span>
            <span className="text-sm text-neutral-400">{formattedDate}</span>
          </div>
          <span className="text-neutral-200">{text}</span>
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
            <CommentInput
              movieId={comment.movieId}
              parentCommentId={comment.parentCommentId}
              replierUserName={comment.user?.name}
              setShowInput={setShowInput}
            />
          )}
          {comment.replies?.map((reply) => <MovieComment comment={reply} key={reply.id} />)}
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

  console.log(comments.content);

  // TODO: implement replies (parentCommentId) -> reply text: @user ... + margin left

  return (
    <div className="mt-5 text-white">
      <span className="text-2xl font-semibold">Comments</span>
      {comments.content.map((comment) => (
        <MovieComment comment={comment} key={comment.id} />
      ))}
    </div>
  );
};
