import { useQuery } from "@tanstack/react-query";
import { MoviePage } from "@/types/movie.ts";

async function getGenre(genreId: string) {
  const response = await fetch(`http://localhost:8088/genres/${genreId}/movies`);
  return await response.json();
}

const useSearchGenreMovies = (genreId: string) => {
  const { data } = useQuery<MoviePage>({
    queryKey: ["genres", genreId, "movies"],
    queryFn: () => getGenre(genreId),
  });

  const movies = data?.content ?? [];

  return { movies };
};

export default useSearchGenreMovies;
