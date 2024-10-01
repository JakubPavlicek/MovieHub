import { useCallback, useMemo } from "react";
import { useQuery } from "@tanstack/react-query";
import type { CountryReponse } from "@/types/country";

async function fetchCountries() {
  const response = await fetch("http://localhost:8088/countries");
  return await response.json();
}

export const useCountries = () => {
  const {
    data,
    isLoading: isLoadingCountries,
    isError: isErrorCountries,
    error: errorCountries,
  } = useQuery<CountryReponse>({
    queryKey: ["countries"],
    queryFn: fetchCountries,
  });

  const { countries, countryMap } = useMemo(() => {
    const countries = data?.countries ?? [];
    const countryMap = new Map(countries.map(({ name, id }) => [name.toLowerCase(), id]));
    return { countries, countryMap };
  }, [data]);

  const getCountryId = useCallback(
    (countryName?: string) => countryName && countryMap.get(countryName.toLowerCase()),
    [countryMap],
  );

  return { countries, countryMap, getCountryId, isLoadingCountries, isErrorCountries, errorCountries };
};
