import { useQuery } from "@tanstack/react-query";
import { MoviePage } from "@/types/movie.ts";

async function fetchMovies() {
  const data = await fetch("http://localhost:8088/movies?limit=20");
  return (await data.json()) as MoviePage;
}

const useMovies = () => {
  const { data, isLoading, isError, error } = useQuery<MoviePage>({
    queryKey: ["movies"],
    queryFn: fetchMovies,
  });

  const movies = data?.content ?? [];

  return { movies, isLoading, isError, error };
};

export default useMovies;
