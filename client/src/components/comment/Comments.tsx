import { type FC, useState } from "react";
import { useApi } from "@/context/ApiProvider";
import { Comment } from "@/components/comment/Comment";
import type { components } from "@/api/api";
import { CommentInput } from "@/components/comment/CommentInput";
import { ChevronLeft, ChevronRight } from "lucide-react";

interface MovieCommentsProps {
  movieId: components["schemas"]["Uuid"];
}

export const Comments: FC<MovieCommentsProps> = ({ movieId }) => {
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
    return <div className="text-white">Empty</div>;
  }

  const selectNewPage = (newPage: number) => {
    if (newPage >= 0 && newPage < comments.totalPages && newPage !== selectedPage) {
      setSelectedPage(newPage);
    }
  };

  const renderPageButton = (page: number) => (
    <button
      key={page}
      className={`inline-flex h-10 w-10 items-center justify-center rounded-md border border-neutral-300 hover:border-cyan-300 hover:text-cyan-300 ${
        selectedPage === page && "border-cyan-200 text-cyan-200"
      }`}
      onClick={() => selectNewPage(page)}
    >
      {page + 1}
    </button>
  );

  const renderEllipsis = (key: string) => (
    <span key={key} className="inline-flex h-10 w-10 items-center justify-center">
      ...
    </span>
  );

  const renderPaginationButtons = () => {
    const totalPages = comments.totalPages;
    const buttons = [];

    for (let i = 0; i < totalPages; i++) {
      if (
        i === 0 || // first page
        i === totalPages - 1 || // last page
        i === selectedPage || // current page
        (i >= selectedPage - 1 && i <= selectedPage + 1) // pages around current page
      ) {
        buttons.push(renderPageButton(i));
      } else if (i === 1 || i === totalPages - 2) {
        buttons.push(renderEllipsis(`ellipsis-${i}`));
      }
    }

    return buttons;
  };

  return (
    <div className="mt-5 text-white">
      <span className="text-2xl font-semibold text-neutral-300">Comments</span>
      {comments.content.map((comment) => (
        <Comment key={comment.id} comment={comment} topLevelCommentId={comment.id} />
      ))}
      {comments.totalPages !== 0 && (
        <div className="my-10 flex flex-row justify-center gap-2.5">
          <button
            className={`rounded-md border border-neutral-300 px-2 ${comments.first ? "opacity-60" : "hover:border-cyan-300 hover:text-cyan-300"}`}
            onClick={() => selectNewPage(selectedPage - 1)}
            disabled={comments.first}
          >
            <ChevronLeft size={20} />
          </button>
          {renderPaginationButtons()}
          <button
            className={`rounded-md border border-neutral-300 px-2 ${comments.last ? "opacity-60" : "hover:border-cyan-300 hover:text-cyan-300"}`}
            onClick={() => selectNewPage(selectedPage + 1)}
            disabled={comments.last}
          >
            <ChevronRight size={20} />
          </button>
        </div>
      )}
      <CommentInput movieId={movieId} />
    </div>
  );
};
