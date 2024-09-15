import { useQuery } from "@tanstack/react-query";
import { MoviePage } from "@/types/movie.ts";

async function fetchMovies() {
  const data = await fetch("http://localhost:8088/movies");
  return (await data.json()) as MoviePage;
}

const useMovies = () => {
  const {
    data: moviePage,
    isLoading,
    isError,
    error,
  } = useQuery<MoviePage>({
    queryKey: ["movies"],
    queryFn: fetchMovies,
  });

  return { moviePage, isLoading, isError, error };
};

export default useMovies;
