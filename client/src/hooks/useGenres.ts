import { useCallback, useMemo } from "react";
import { useApi } from "@/context/ApiProvider";
import { useNavigate } from "react-router-dom";

export const useGenres = () => {
  const navigate = useNavigate();
  const api = useApi();
  const { data } = api.useQuery("get", "/genres");

  const { genres, genreMap } = useMemo(() => {
    const genres = data?.genres ?? [];
    const genreMap = new Map(genres.map(({ name, id }) => [name, id]));
    return { genres, genreMap };
  }, [data]);

  const getGenreId = useCallback(
    (genreName: string) => {
      if (!genreMap.has(genreName)) {
        navigate("/", { replace: true });
      }
      return genreMap.get(genreName);
    },
    [genreMap, navigate],
  );

  return { genres, genreMap, getGenreId };
};
