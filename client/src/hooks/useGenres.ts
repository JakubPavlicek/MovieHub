import { useCallback, useMemo } from "react";
import { useApi } from "@/context/ApiProvider";

export const useGenres = () => {
  const api = useApi();
  const { data } = api.useQuery("get", "/genres");

  const { genres, genreMap } = useMemo(() => {
    const genres = data?.genres ?? [];
    const genreMap = new Map(genres.map(({ name, id }) => [name?.toLowerCase(), id]));
    return { genres, genreMap };
  }, [data]);

  const getGenreId = useCallback(
    (genreName?: string) => genreName && genreMap.get(genreName.toLowerCase()),
    [genreMap],
  );

  return { genres, genreMap, getGenreId };
};
