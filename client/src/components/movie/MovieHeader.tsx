import { type FC } from "react";
import { StarRating } from "@/components/movie/StarRating";
import { Star } from "lucide-react";
import type { components } from "@/api/api";

interface MovieStatsProps {
  rating: components["schemas"]["MovieDetailsResponse"]["rating"];
  duration: components["schemas"]["MovieDetailsResponse"]["duration"];
}

interface MovieRatingProps {
  movieId: components["schemas"]["MovieDetailsResponse"]["id"];
  userRating: components["schemas"]["MovieDetailsResponse"]["userRating"];
  reviewCount: components["schemas"]["MovieDetailsResponse"]["reviewCount"];
}

interface MovieHeaderProps {
  movieDetails: components["schemas"]["MovieDetailsResponse"];
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

const MovieRating: FC<MovieRatingProps> = ({ movieId, userRating, reviewCount }) => {
  return (
    <div className="flex min-w-max gap-1">
      <StarRating movieId={movieId} userRating={userRating} />
      <span>({reviewCount} reviews)</span>
    </div>
  );
};

export const MovieHeader: FC<MovieHeaderProps> = ({ movieDetails }) => {
  if (!movieDetails) {
    return <div>Empty</div>;
  }

  const { id, name, rating, duration, reviewCount, userRating } = movieDetails;

  return (
    <div className="flex flex-wrap items-center justify-between gap-2">
      <div className="flex flex-col gap-2">
        <h1 className="text-4xl font-medium text-neutral-300">{name}</h1>
        <MovieStats rating={rating} duration={duration} />
      </div>
      <MovieRating movieId={id} userRating={userRating} reviewCount={reviewCount} />
    </div>
  );
};
