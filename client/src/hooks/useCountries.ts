import { useCallback, useMemo } from "react";
import { useApi } from "@/context/ApiProvider";

export const useCountries = () => {
  const api = useApi();
  const { data, isLoading } = api.useQuery("get", "/countries");

  const { countries, countryMap } = useMemo(() => {
    if (!data?.countries) return { countries: [], countryMap: new Map() };

    const countryMap = new Map(data.countries.map(({ name, id }) => [name, id]));
    return { countries: data.countries, countryMap };
  }, [data]);

  const getCountryId = useCallback(
    (countryName: string | undefined) => {
      if (!countryName || isLoading) return null;

      return countryMap.get(countryName) || null;
    },
    [countryMap, isLoading],
  );

  return { countries, countryMap, getCountryId, isLoading };
};
