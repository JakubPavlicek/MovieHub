import type { FC } from "react";
import type { components } from "@/api/api";
import { ChevronLeft, ChevronRight } from "lucide-react";

interface PaginationProps {
  selectedPage: number;
  onPageChange: (newPage: number) => void;
  totalPages: components["schemas"]["CommentInfoPage"]["totalPages"];
}

export const Pagination: FC<PaginationProps> = ({ selectedPage, onPageChange, totalPages }) => {
  const renderPageButton = (page: number) => (
    <button
      key={page}
      className={`inline-flex h-10 w-10 items-center justify-center rounded-md border hover:border-cyan-300 hover:text-cyan-300 ${
        selectedPage === page ? "border-cyan-300 text-cyan-300" : "border-neutral-300"
      }`}
      onClick={() => onPageChange(page)}
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
    <div className="my-10 flex flex-row justify-center gap-2.5">
      <button
        className={`rounded-md border border-neutral-300 px-2 ${selectedPage === 0 ? "opacity-60" : "hover:border-cyan-300 hover:text-cyan-300"}`}
        onClick={() => onPageChange(selectedPage - 1)}
        disabled={selectedPage === 0}
      >
        <ChevronLeft size={20} />
      </button>
      {renderPaginationButtons()}
      <button
        className={`rounded-md border border-neutral-300 px-2 ${selectedPage === totalPages - 1 ? "opacity-60" : "hover:border-cyan-300 hover:text-cyan-300"}`}
        onClick={() => onPageChange(selectedPage + 1)}
        disabled={selectedPage === totalPages - 1}
      >
        <ChevronRight size={20} />
      </button>
    </div>
  );
};
