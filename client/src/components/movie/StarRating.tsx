import { FC, useEffect, useState } from "react";
import { Star } from "lucide-react";
import { useAddMovieRating } from "@/hooks/useAddMovieRating";
import { useAuth0 } from "@auth0/auth0-react";
import { useGetMovieRating } from "@/hooks/useGetMovieRating";

interface StarRatingProps {
  movieId: string;
}

const StarRating: FC<StarRatingProps> = ({ movieId }) => {
  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);
  const { isAuthenticated, getAccessTokenSilently } = useAuth0();
  const [token, setToken] = useState<string | null>(null);

  const { movieRating } = useGetMovieRating({ movieId, token });
  const { rateMovie } = useAddMovieRating(movieId);

  useEffect(() => {
    const fetchToken = async () => {
      if (!isAuthenticated) return;

      const fetchedToken = await getAccessTokenSilently();
      setToken(fetchedToken);
    };
    fetchToken();
  }, [isAuthenticated, getAccessTokenSilently]);

  useEffect(() => {
    if (movieRating) {
      setRating(movieRating.rating / 2);
    }
  }, [movieRating]);

  const handleRating = async (starIndex: number) => {
    if (!isAuthenticated) return;

    const token = await getAccessTokenSilently();
    setRating(starIndex);
    const userRating = starIndex * 2;
    await rateMovie({ movieId, userRating, token });
  };

  return (
    <div className="flex text-white">
      {Array.from({ length: 5 }, (_, starIndex) => {
        starIndex++;

        return (
          <Star
            key={starIndex}
            className={`cursor-pointer ${starIndex <= (hoverRating || rating) ? "fill-amber-300" : "fill-neutral-500"}`}
            strokeWidth={0}
            onMouseEnter={() => setHoverRating(starIndex)}
            onMouseLeave={() => setHoverRating(0)}
            onClick={() => handleRating(starIndex)}
          />
        );
      })}
    </div>
  );
};

export default StarRating;
