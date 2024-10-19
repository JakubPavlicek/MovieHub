import { useApi } from "@/context/ApiProvider";
import { useRedirectIfGenreNotFound } from "@/hooks/useRedirectIfGenreNotFound";

export const useFetchMoviesByGenre = (genreName: string | undefined) => {
  const genreId = useRedirectIfGenreNotFound(genreName);

  const api = useApi();
  const { data: movies } = api.useQuery(
    "get",
    "/genres/{genreId}/movies",
    {
      params: {
        path: { genreId: genreId },
      },
    },
    {
      enabled: !!genreId,
    },
  );

  return movies;
};
