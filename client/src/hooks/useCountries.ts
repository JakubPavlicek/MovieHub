import { useQuery } from "@tanstack/react-query";
import { CountryReponse } from "@/types/country.ts";

async function fetchCountries() {
  const response = await fetch("http://localhost:8088/countries");
  return await response.json();
}

const useCountries = () => {
  const {
    data,
    isLoading: isLoadingCountries,
    isError: isErrorCountries,
    error: errorCountries,
  } = useQuery<CountryReponse>({
    queryKey: ["countries"],
    queryFn: fetchCountries,
  });

  const countries = data?.countries ?? [];

  return { countries, isLoadingCountries, isErrorCountries, errorCountries };
};

export default useCountries;
