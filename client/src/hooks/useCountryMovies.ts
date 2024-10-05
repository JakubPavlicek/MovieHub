import { useApi } from "@/context/ApiProvider";

export const useCountryMovies = (countryId: string) => {
  const api = useApi();
  const { data } = api.useQuery(
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

  const movies = data?.content ?? [];

  return { movies };
};
