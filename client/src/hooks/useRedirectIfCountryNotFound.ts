import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useCountries } from "@/hooks/useCountries";

export const useRedirectIfCountryNotFound = (countryName: string | undefined) => {
  const navigate = useNavigate();
  const { getCountryId, isLoading } = useCountries();
  const countryId = getCountryId(countryName);

  useEffect(() => {
    if (!countryId && !isLoading) {
      navigate("/", { replace: true });
    }
  }, [countryId, isLoading, navigate]);

  return countryId;
};
