import type { FC } from "react";
import type { components } from "@/api/api";
import { useApi } from "@/context/ApiProvider";
import { CircleUserRound, ThumbsDown, ThumbsUp } from "lucide-react";

interface MovieCommentProps {
  comment: components["schemas"]["CommentDetailsResponse"];
}

const MovieComment: FC<MovieCommentProps> = ({ comment }) => {
  const { userId, createdAt, text } = comment;

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
    <div className="mt-6 flex flex-row gap-4">
      <CircleUserRound size={52} strokeWidth={1.3} className="text-cyan-300" />
      <div className="flex w-full flex-col gap-1.5">
        <div className="flex flex-row items-center gap-3">
          <span className="font-semibold">{userId?.split("|")[0]}</span>
          <span className="text-sm text-neutral-400">{formattedDate}</span>
        </div>
        <span className="text-neutral-200">{text}</span>
        <div className="flex flex-row items-center justify-between">
          <button className="max-w-fit text-neutral-400 hover:text-neutral-200">Reply</button>
          <div className="flex flex-row gap-4">
            <div className="flex flex-row items-center gap-1 text-neutral-300">
              <span>0</span>
              <button className="rounded-full p-1 hover:text-white">
                <ThumbsUp size={20} strokeWidth={2} />
              </button>
            </div>
            <div className="flex flex-row items-center gap-1 text-neutral-300">
              <span>4</span>
              <button className="p-1 hover:text-white">
                <ThumbsDown size={20} strokeWidth={2} />
              </button>
            </div>
          </div>
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
    },
  });

  if (!comments?.content) {
    return <div className="text-white">Empty</div>;
  }

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
