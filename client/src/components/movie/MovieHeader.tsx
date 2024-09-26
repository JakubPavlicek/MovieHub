import type { FC } from "react";
import { Star } from "lucide-react";
import StarRating from "@/components/movie/StarRating";
import type { MovieDetails } from "@/types/movie";

interface MovieStatsProps {
  rating: number;
  duration: number;
}

interface MovieRatingProps {
  movieId: string;
  reviewCount: number;
}

interface MovieHeaderProps {
  movieDetails: MovieDetails;
}

const MovieStats: FC<MovieStatsProps> = ({ rating, duration }) => {
  return (
    <div className="flex items-center gap-2">
      <Star size={20} className="fill-neutral-400" />
      <span>{rating}</span>
      <span className="ml-3">{duration} min</span>
    </div>
  );
};

const MovieRating: FC<MovieRatingProps> = ({ movieId, reviewCount }) => {
  return (
    <div className="flex min-w-max gap-1">
      <StarRating movieId={movieId} />
      <span>({reviewCount} reviews)</span>
    </div>
  );
};

const MovieHeader: FC<MovieHeaderProps> = ({ movieDetails }) => {
  const { id, name, rating, duration, reviewCount } = movieDetails;

  return (
    <div className="flex flex-wrap items-center justify-between gap-2">
      <div className="flex flex-col gap-2">
        <h1 className="text-4xl font-medium">{name}</h1>
        <MovieStats rating={rating} duration={duration} />
      </div>
      <MovieRating movieId={id} reviewCount={reviewCount} />
    </div>
  );
};

export default MovieHeader;
