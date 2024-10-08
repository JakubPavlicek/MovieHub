import { type FC } from "react";
import { useApi } from "@/context/ApiProvider";
import { MovieComment } from "@/components/comment/MovieComment";
import type { components } from "@/api/api";
import { CommentInput } from "@/components/comment/CommentInput";

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

  console.log(comments);

  return (
    <div className="mt-5 text-white">
      <span className="text-2xl font-semibold text-neutral-300">Comments</span>
      {comments.content.map((comment) => (
        <MovieComment key={comment.id} comment={comment} topLevelCommentId={comment.id} />
      ))}
      <CommentInput movieId={movieId} />
    </div>
  );
};
