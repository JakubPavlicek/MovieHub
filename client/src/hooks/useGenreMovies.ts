import { useQuery } from "@tanstack/react-query";
import type { MoviePage } from "@/types/movie";

async function getGenre(genreId: string) {
  const response = await fetch(`http://localhost:8088/genres/${genreId}/movies`);
  return await response.json();
}

export const useGenreMovies = (genreId: string | undefined) => {
  const { data } = useQuery<MoviePage>({
    queryKey: ["genres", genreId, "movies"],
    queryFn: () => getGenre(genreId as string),
    enabled: !!genreId,
  });

  const movies = data?.content ?? [];

  return { movies };
};
