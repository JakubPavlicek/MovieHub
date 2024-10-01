import { useCallback, useMemo } from "react";
import { useQuery } from "@tanstack/react-query";
import type { GenreResponse } from "@/types/genre";

async function fetchGenres() {
  const response = await fetch("http://localhost:8088/genres");
  return await response.json();
}

export const useGenres = () => {
  const {
    data,
    isLoading: isLoadingGenres,
    isError: isGenresError,
    error: errorGenres,
  } = useQuery<GenreResponse>({
    queryKey: ["genres"],
    queryFn: fetchGenres,
  });

  const { genres, genreMap } = useMemo(() => {
    const genres = data?.genres ?? [];
    const genreMap = new Map(genres.map(({ name, id }) => [name.toLowerCase(), id]));
    return { genres, genreMap };
  }, [data]);

  const getGenreId = useCallback(
    (genreName?: string) => genreName && genreMap.get(genreName.toLowerCase()),
    [genreMap],
  );

  return { genres, genreMap, getGenreId, isLoadingGenres, isGenresError, errorGenres };
};
