import { useQuery } from "@tanstack/react-query";
import { MovieDetails } from "@/types/movie.ts";

async function fetchMovieDetails(movieId: string): Promise<MovieDetails> {
  const response = await fetch(`http://localhost:8088/movies/${movieId}`);
  return await response.json();
}

const useMovieDetails = (movieId: string) => {
  const { data: movieDetails } = useQuery<MovieDetails>({
    queryKey: ["movies", movieId],
    queryFn: () => fetchMovieDetails(movieId),
  });

  return { movieDetails };
};

export default useMovieDetails;
