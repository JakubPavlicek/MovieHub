import { useCallback, useMemo } from "react";
import { useApi } from "@/context/ApiProvider";

export const useCountries = () => {
  const api = useApi();
  const { data } = api.useQuery("get", "/countries");

  const { countries, countryMap } = useMemo(() => {
    const countries = data?.countries ?? [];
    const countryMap = new Map(countries.map(({ name, id }) => [name?.toLowerCase(), id]));
    return { countries, countryMap };
  }, [data]);

  const getCountryId = useCallback(
    (countryName?: string) => countryName && countryMap.get(countryName.toLowerCase()),
    [countryMap],
  );

  return { countries, countryMap, getCountryId };
};
