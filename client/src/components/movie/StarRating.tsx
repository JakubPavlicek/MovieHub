import { type FC, useEffect, useState } from "react";
import { Star } from "lucide-react";
import type { components } from "@/api/api";
import { useApi } from "@/context/ApiProvider";
import { useQueryClient } from "@tanstack/react-query";

interface StarRatingProps {
  movieId: components["schemas"]["MovieDetailsResponse"]["id"];
}

export const StarRating: FC<StarRatingProps> = ({ movieId = "" }) => {
  const multiplier = 2;
  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);
  const queryClient = useQueryClient();
  const api = useApi();
  const { data: movieRating } = api.useQuery("get", "/movies/{movieId}/ratings", {
    params: {
      path: { movieId: movieId },
    },
  });
  const { mutate } = api.useMutation("post", "/movies/{movieId}/ratings", {
    onSuccess: async () => queryClient.invalidateQueries({ queryKey: ["get", "/movies/{movieId}"] }),
  });

  useEffect(() => {
    if (!movieRating) return;
    if (!movieRating.rating) return;
    setRating(movieRating.rating / multiplier);
  }, [movieRating]);

  const submitRating = async (starIndex: number) => {
    setRating(starIndex);
    mutate({
      params: {
        path: { movieId: movieId },
      },
      body: { rating: starIndex * multiplier },
    });
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
