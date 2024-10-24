import { type FC } from "react";
import { StarRating } from "@/components/movie/StarRating";
import { Star } from "lucide-react";
import type { components } from "@/api/api";
import { useTranslation } from "react-i18next";

interface MovieStatsProps {
  rating: components["schemas"]["MovieDetailsResponse"]["rating"];
  duration: components["schemas"]["MovieDetailsResponse"]["duration"];
}

const MovieStats: FC<MovieStatsProps> = ({ rating, duration }) => {
  const { t } = useTranslation();
  return (
    <div className="flex items-center gap-2">
      <Star size={20} className="fill-neutral-400" />
      <span>{rating}</span>
      <span className="ml-3">
        {duration}
        {t("components.movie.minuteDuration")}
      </span>
    </div>
  );
};

interface MovieRatingProps {
  movieId: components["schemas"]["MovieDetailsResponse"]["id"];
  reviewCount: components["schemas"]["MovieDetailsResponse"]["reviewCount"];
}

const MovieRating: FC<MovieRatingProps> = ({ movieId, reviewCount }) => {
  const { t } = useTranslation();

  return (
    <div className="flex min-w-max gap-1">
      <StarRating movieId={movieId} />
      <span>({t("components.movie.reviews", { count: reviewCount })})</span>
    </div>
  );
};

interface MovieHeaderProps {
  movieDetails: components["schemas"]["MovieDetailsResponse"];
}

export const MovieHeader: FC<MovieHeaderProps> = ({ movieDetails }) => {
  if (!movieDetails) {
    return <div></div>;
  }

  const { id, name, rating, duration, reviewCount } = movieDetails;

  return (
    <div className="flex flex-wrap items-center justify-between gap-2">
      <div className="flex flex-col gap-2">
        <h1 className="text-4xl font-medium text-neutral-300">{name}</h1>
        <MovieStats rating={rating} duration={duration} />
      </div>
      <MovieRating movieId={id} reviewCount={reviewCount} />
    </div>
  );
};
