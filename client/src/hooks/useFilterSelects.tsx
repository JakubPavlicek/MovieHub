import { useCallback, useEffect, useState } from "react";
import { MultiValue } from "react-select";
import type { SelectOption } from "@/types/selectOption";
import { useNavigate, useSearchParams } from "react-router-dom";

const parseSearchParams = (param: string | null): MultiValue<SelectOption> => {
  return param && param !== "all" ? param.split(",").map((item) => ({ value: item, label: item })) : [];
};

const genresParam = "genres";
const countriesParam = "countries";
const releaseYearsParam = "releaseYears";

export const useFilterSelects = (isShowFilters: boolean, isNavigate: boolean) => {
  const [showFilters, setShowFilters] = useState(isShowFilters);
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  const getSelectedOptions = useCallback((selectedValues: MultiValue<SelectOption>): string => {
    return selectedValues.length > 0 ? selectedValues.map((selectedValue) => selectedValue.value).join(",") : "all";
  }, []);

  const [selectedGenres, setSelectedGenres] = useState<MultiValue<SelectOption>>(
    parseSearchParams(searchParams.get(genresParam)),
  );
  const [selectedCountries, setSelectedCountries] = useState<MultiValue<SelectOption>>(
    parseSearchParams(searchParams.get(countriesParam)),
  );
  const [selectedYears, setSelectedYears] = useState<MultiValue<SelectOption>>(
    parseSearchParams(searchParams.get(releaseYearsParam)),
  );

  useEffect(() => {
    const params = new URLSearchParams();

    if (selectedGenres.length) {
      params.set(genresParam, getSelectedOptions(selectedGenres));
    }
    if (selectedCountries.length) {
      params.set(countriesParam, getSelectedOptions(selectedCountries));
    }
    if (selectedYears.length) {
      params.set(releaseYearsParam, getSelectedOptions(selectedYears));
    }

    if (params.toString() !== searchParams.toString()) {
      if (isNavigate) {
        navigate(`/filter?${params.toString()}`, { replace: true });
      } else {
        setSearchParams(params);
      }
    }
  }, [selectedGenres, selectedCountries, selectedYears, searchParams, setSearchParams, navigate, isNavigate]);

  return {
    selectedGenres,
    setSelectedGenres,
    selectedCountries,
    setSelectedCountries,
    selectedYears,
    setSelectedYears,
    showFilters,
    setShowFilters,
    getSelectedOptions,
  };
};
