import { useQuery } from "@tanstack/react-query";
import { DropdownItem } from "../components/header/components/DropdownMenu.tsx";

type Country = DropdownItem;

interface CountryReponse {
  countries: Country[];
}

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
