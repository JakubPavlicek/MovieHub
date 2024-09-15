import type { FC } from "react";
import { MoviePreview } from "@/types/movie.ts";

interface MoviePreviewProps {
  moviePreview: MoviePreview;
}

const MoviePreviewCard: FC<MoviePreviewProps> = ({ moviePreview }) => {
  return (
    <div className="flex flex-col gap-2">
      <img src={moviePreview.posterUrl} alt={moviePreview.name} className="rounded-md" />
      <div className="flex flex-col gap-0.5 text-sm sm:text-base">
        <span className="mt-1 truncate font-medium">{moviePreview.name}</span>
        <div className="space-x-1.5 truncate opacity-50">
          <span>{moviePreview.releaseYear}</span>
          <span>&#8226;</span>
          <span>{moviePreview.duration}m</span>
        </div>
        <span className="truncate opacity-70">{moviePreview.genres.join(", ")}</span>
      </div>
    </div>
  );
};

export default MoviePreviewCard;
