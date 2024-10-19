import { type FC, useCallback, useEffect, useMemo, useState } from "react";
import { Star } from "lucide-react";
import type { components } from "@/api/api";
import { useApi } from "@/context/ApiProvider";
import { useQueryClient } from "@tanstack/react-query";
import { useAuth0 } from "@auth0/auth0-react";
import { toast } from "react-toastify";
import { useTranslation } from "react-i18next";

interface StarRatingProps {
  movieId: components["schemas"]["MovieDetailsResponse"]["id"];
}

const multiplier = 2;

export const StarRating: FC<StarRatingProps> = ({ movieId = "" }) => {
  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);
  const { t } = useTranslation();
  const { isAuthenticated } = useAuth0();
  const queryClient = useQueryClient();

  const api = useApi();

  const { data: userRating, isFetched } = api.useQuery(
    "get",
    "/movies/{movieId}/ratings/me",
    {
      params: {
        path: { movieId: movieId },
      },
    },
    {
      enabled: isAuthenticated,
    },
  );

  useEffect(() => {
    if (isFetched) setRating(userRating!.rating / multiplier);
  }, [isFetched, userRating]);

  const { mutate } = api.useMutation("post", "/movies/{movieId}/ratings", {
    onSuccess: async () => queryClient.invalidateQueries({ queryKey: ["get", "/movies/{movieId}"] }),
  });

  const submitRating = useCallback(
    async (starIndex: number) => {
      if (!isAuthenticated) {
        toast.error(t("toast.unauthenticated"));
        return;
      }

      setRating(starIndex);

      mutate({
        params: {
          path: { movieId: movieId },
        },
        body: { rating: starIndex * multiplier },
      });
    },
    [isAuthenticated, movieId, mutate],
  );

  const renderStars = useMemo(
    () =>
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
      }),
    [hoverRating, rating, submitRating],
  );

  return <div className="flex text-white">{renderStars}</div>;
};
