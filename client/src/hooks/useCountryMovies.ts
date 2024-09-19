import { useQuery } from "@tanstack/react-query";
import { MoviePage } from "@/types/movie.ts";

async function getCountry(countryId: string) {
  const response = await fetch(`http://localhost:8088/countries/${countryId}/movies`);
  return await response.json();
}

const useCountryMovies = (countryId: string | undefined) => {
  const { data } = useQuery<MoviePage>({
    queryKey: ["countries", countryId, "movies"],
    queryFn: () => getCountry(countryId as string),
    enabled: !!countryId,
  });

  const movies = data?.content ?? [];

  return { movies };
};

export default useCountryMovies;
