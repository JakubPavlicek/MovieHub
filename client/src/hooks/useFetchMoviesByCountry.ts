import { useApi } from "@/context/ApiProvider";
import { useRedirectIfCountryNotFound } from "@/hooks/useRedirectIfCountryNotFound";

export const useFetchMoviesByCountry = (countryName: string | undefined) => {
  const countryId = useRedirectIfCountryNotFound(countryName);

  const api = useApi();
  const { data: movies } = api.useQuery(
    "get",
    "/countries/{countryId}/movies",
    {
      params: {
        path: { countryId: countryId },
      },
    },
    {
      enabled: !!countryId,
    },
  );

  return movies;
};
