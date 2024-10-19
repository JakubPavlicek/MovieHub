import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useGenres } from "@/hooks/useGenres";

export const useRedirectIfGenreNotFound = (genreName: string | undefined) => {
  const navigate = useNavigate();
  const { getGenreId, isLoading } = useGenres();
  const genreId = getGenreId(genreName);

  useEffect(() => {
    if (!genreId && !isLoading) {
      navigate("/", { replace: true });
    }
  }, [genreId, isLoading, navigate]);

  return genreId;
};
