import type { FC } from "react";
import { Link } from "react-router-dom";
import { Play } from "lucide-react";
import { MoviePreview } from "@/types/movie";

interface MoviePreviewProps {
  moviePreview: MoviePreview;
}

const MoviePreviewCard: FC<MoviePreviewProps> = ({ moviePreview }) => {
  const destination = `/movie/${moviePreview.id}`;

  return (
    <div className="flex flex-col gap-2">
      <div className="group relative">
        <Link to={destination}>
          <img
            src={moviePreview.posterUrl}
            alt={moviePreview.name}
            className="transform rounded-md opacity-100 duration-300 group-hover:opacity-50"
          />
          <button className="absolute left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 transform rounded-full bg-cyan-500 p-4 opacity-0 duration-300 group-hover:opacity-100">
            <Play />
          </button>
        </Link>
      </div>
      <div className="flex flex-col gap-0.5 text-sm sm:text-base">
        <Link to={destination} className="mt-1 max-w-min truncate font-medium hover:underline">
          {moviePreview.name}
        </Link>
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
