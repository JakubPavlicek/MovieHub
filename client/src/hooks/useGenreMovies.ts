import { useQuery } from "@tanstack/react-query";
import { MoviePage } from "@/types/movie.ts";

async function getGenre(genreId: string) {
  const response = await fetch(`http://localhost:8088/genres/${genreId}/movies`);
  return await response.json();
}

const useGenreMovies = (genreId: string | undefined) => {
  const { data } = useQuery<MoviePage>({
    queryKey: ["genres", genreId, "movies"],
    queryFn: () => getGenre(genreId as string),
    enabled: !!genreId,
  });

  const movies = data?.content ?? [];

  return { movies };
};

export default useGenreMovies;
