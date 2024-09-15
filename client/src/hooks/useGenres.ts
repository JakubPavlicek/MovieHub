import { useQuery } from "@tanstack/react-query";
import { GenreResponse } from "@/types/genre.ts";

async function fetchGenres() {
  const response = await fetch("http://localhost:8088/genres");
  return await response.json();
}

const useGenres = () => {
  const {
    data,
    isLoading: isLoadingGenres,
    isError: isGenresError,
    error: errorGenres,
  } = useQuery<GenreResponse>({
    queryKey: ["genres"],
    queryFn: fetchGenres,
  });

  const genres = data?.genres ?? [];

  return { genres, isLoadingGenres, isGenresError, errorGenres };
};

export default useGenres;
