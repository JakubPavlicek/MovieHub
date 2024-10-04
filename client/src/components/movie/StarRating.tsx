import { type FC, useEffect, useState } from "react";
import { Star } from "lucide-react";
import { useAddMovieRating } from "@/hooks/useAddMovieRating";
import type { components } from "@/api/api";
import { useApi } from "@/context/ApiProvider";

interface StarRatingProps {
  movieId: components["schemas"]["MovieDetailsResponse"]["id"] | undefined;
  token: string | null;
}

export const StarRating: FC<StarRatingProps> = ({ movieId = "", token }) => {
  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);
  const api = useApi();
  const { data: movieRating } = api.useQuery("get", "/movies/{movieId}/ratings", {
    params: {
      path: { movieId: movieId },
    },
  });
  const { rateMovie } = useAddMovieRating(movieId);
  const multiplier = 2;

  useEffect(() => {
    if (!movieRating) return;
    if (!movieRating.rating) return;
    setRating(movieRating.rating / multiplier);
  }, [movieRating]);

  const submitRating = async (starIndex: number) => {
    if (!token) return;
    setRating(starIndex);
    const userRating = starIndex * multiplier;
    await rateMovie({ movieId, userRating, token });
  };

  const renderStars = () =>
    Array.from({ length: 5 }, (_, index) => {
      const starIndex = index + 1;
      const isFilled = starIndex <= (hoverRating || rating);

      return (
        <Star
          key={starIndex}
          className={`cursor-pointer ${isFilled ? "fill-amber-300" : "fill-neutral-500"}`}
          strokeWidth={0}
          onMouseEnter={() => setHoverRating(starIndex)}
          onMouseLeave={() => setHoverRating(0)}
          onClick={() => submitRating(starIndex)}
        />
      );
    });

  return <div className="flex text-white">{renderStars()}</div>;
};
