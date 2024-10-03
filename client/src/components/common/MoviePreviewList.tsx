import type { FC } from "react";
import { MoviePreviewCard } from "@/components/common/MoviePreviewCard";
import type { components } from "@/api/api";

interface MoviePreviewListProps {
  movies: components["schemas"]["MoviePreviewResponse"][] | undefined;
}

export const MoviePreviewList: FC<MoviePreviewListProps> = ({ movies }) => {
  return (
    <div className="grid grid-cols-[repeat(auto-fill,minmax(160px,1fr))] gap-x-3.5 gap-y-10">
      {movies?.map((movie) => <MoviePreviewCard key={movie.id} moviePreview={movie} />)}
    </div>
  );
};
