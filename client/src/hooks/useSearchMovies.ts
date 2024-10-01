import { useQuery } from "@tanstack/react-query";
import type { MoviePage, MovieSearchParams } from "@/types/movie";

async function fetchMovies(params: MovieSearchParams): Promise<MoviePage> {
  const query = new URLSearchParams(params as Record<string, string>).toString();
  const response = await fetch(`http://localhost:8088/movies?${query}`);
  return await response.json();
}

export const useSearchMovies = (params: MovieSearchParams) => {
  const { data } = useQuery<MoviePage>({
    queryKey: ["movies", params],
    queryFn: () => fetchMovies(params),
  });

  const movies = data?.content ?? [];

  return { movies };
};
