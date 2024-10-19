import { useCallback, useMemo } from "react";
import { useApi } from "@/context/ApiProvider";

export const useGenres = () => {
  const api = useApi();
  const { data, isLoading } = api.useQuery("get", "/genres");

  const { genres, genreMap } = useMemo(() => {
    if (!data?.genres) return { genres: [], genreMap: new Map() };

    const genreMap = new Map(data.genres.map(({ name, id }) => [name, id]));
    return { genres: data.genres, genreMap };
  }, [data]);

  const getGenreId = useCallback(
    (genreName: string | undefined) => {
      if (!genreName || isLoading) return null;

      return genreMap.get(genreName) || null;
    },
    [genreMap, isLoading],
  );

  return { genres, genreMap, getGenreId, isLoading };
};
