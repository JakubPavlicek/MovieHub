import { useCallback, useMemo } from "react";
import { useApi } from "@/context/ApiProvider";
import { useNavigate } from "react-router-dom";

export const useCountries = () => {
  const navigate = useNavigate();
  const api = useApi();
  const { data } = api.useQuery("get", "/countries");

  const { countries, countryMap } = useMemo(() => {
    const countries = data?.countries ?? [];
    const countryMap = new Map(countries.map(({ name, id }) => [name, id]));
    return { countries, countryMap };
  }, [data]);

  const getCountryId = useCallback(
    (countryName: string) => {
      if (!countryMap.has(countryName)) {
        navigate("/", { replace: true });
      }
      return countryMap.get(countryName);
    },
    [countryMap, navigate],
  );

  return { countries, countryMap, getCountryId };
};
