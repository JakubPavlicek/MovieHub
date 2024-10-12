import { type FC } from "react";
import { StarRating } from "@/components/movie/StarRating";
import { Star } from "lucide-react";
import type { components } from "@/api/api";
import { useApi } from "@/context/ApiProvider";
import { useTranslation } from "react-i18next";

interface MovieStatsProps {
  rating: components["schemas"]["MovieDetailsResponse"]["rating"];
  duration: components["schemas"]["MovieDetailsResponse"]["duration"];
}

interface MovieRatingProps {
  movieId: components["schemas"]["MovieDetailsResponse"]["id"];
  reviewCount: components["schemas"]["MovieDetailsResponse"]["reviewCount"];
}

interface MovieHeaderProps {
  movieDetails: components["schemas"]["MovieDetailsResponse"];
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

const MovieRating: FC<MovieRatingProps> = ({ movieId, reviewCount }) => {
  const { t } = useTranslation();
  const api = useApi();
  const { data: userRating } = api.useQuery("get", "/movies/{movieId}/ratings/me", {
    params: {
      path: { movieId: movieId },
    },
  });

  if (!userRating) {
    return <></>;
  }

  return (
    <div className="flex min-w-max gap-1">
      <StarRating movieId={movieId} userRating={userRating.rating} />
      <span>({t("components.movie.reviews", { count: reviewCount })})</span>
    </div>
  );
};

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
