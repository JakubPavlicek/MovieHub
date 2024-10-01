import { useQuery } from "@tanstack/react-query";
import type { MoviePage } from "@/types/movie";

async function getCountry(countryId: string) {
  const response = await fetch(`http://localhost:8088/countries/${countryId}/movies`);
  return await response.json();
}

export const useCountryMovies = (countryId: string | undefined) => {
  const { data } = useQuery<MoviePage>({
    queryKey: ["countries", countryId, "movies"],
    queryFn: () => getCountry(countryId as string),
    enabled: !!countryId,
  });

  const movies = data?.content ?? [];

  return { movies };
};
