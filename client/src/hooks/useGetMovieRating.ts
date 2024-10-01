import { useQuery } from "@tanstack/react-query";
import type { MovieRating } from "@/types/movie";

interface MovieRatingPayload {
  movieId: string;
  token: string | null;
}

const getMovieRating = async ({ movieId, token }: MovieRatingPayload) => {
  const response = await fetch(`http://localhost:8088/movies/${movieId}/ratings`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return (await response.json()) as MovieRating;
};

export const useGetMovieRating = ({ movieId, token }: MovieRatingPayload) => {
  const { data: movieRating } = useQuery<MovieRating>({
    queryKey: ["movies", movieId, "rating"],
    queryFn: () => getMovieRating({ movieId, token }),
    enabled: !!movieId && !!token,
  });

  return { movieRating };
};
