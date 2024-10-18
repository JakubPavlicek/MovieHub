import { type FC, useState } from "react";
import type { components } from "@/api/api";
import { useApi } from "@/context/ApiProvider";
import { Comment } from "@/components/comment/Comment";
import { CommentInput } from "@/components/comment/CommentInput";
import { Pagination } from "@/components/comment/Pagination";
import { useTranslation } from "react-i18next";

interface MovieCommentsProps {
  movieId: components["schemas"]["Uuid"];
}

export const Comments: FC<MovieCommentsProps> = ({ movieId }) => {
  const { t } = useTranslation();
  const [selectedPage, setSelectedPage] = useState(0);

  const api = useApi();
  const { data: comments } = api.useQuery("get", "/movies/{movieId}/comments", {
    params: {
      path: { movieId: movieId },
      query: {
        page: selectedPage,
        limit: 5,
      },
    },
  });

  if (!comments?.content) {
    return <div></div>;
  }

  const selectNewPage = (newPage: number) => {
    if (newPage >= 0 && newPage < comments.totalPages && newPage !== selectedPage) {
      setSelectedPage(newPage);
    }
  };

  return (
    <div className="mt-5 text-white">
      <span className="text-2xl font-semibold text-neutral-300">{t("components.comment.title")}</span>
      {comments.content.map((comment) => (
        <Comment key={comment.id} comment={comment} movieId={movieId} />
      ))}
      {comments.totalPages !== 0 && (
        <Pagination selectedPage={selectedPage} onPageChange={selectNewPage} totalPages={comments.totalPages} />
      )}
      <CommentInput movieId={movieId} />
    </div>
  );
};
