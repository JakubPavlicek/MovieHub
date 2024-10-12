import { useEffect, useState } from "react";
import { MultiValue } from "react-select";
import type { SelectOption } from "@/types/selectOption";
import { useNavigate, useSearchParams } from "react-router-dom";
import { filterAll, filterParams } from "@/types/filterParams";

const parseSearchParams = (param: string | null): MultiValue<SelectOption> => {
  return param && param !== "all" ? param.split(",").map((item) => ({ value: item, label: item })) : [];
};

const formatSelectedOptions = (selectedValues: MultiValue<SelectOption>): string => {
  return selectedValues.length > 0 ? selectedValues.map((selectedValue) => selectedValue.value).join(",") : filterAll;
};

export const useFilterSelects = (isNavigate: boolean) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  const [selectedGenres, setSelectedGenres] = useState<MultiValue<SelectOption>>(
    parseSearchParams(searchParams.get(filterParams.genres)),
  );
  const [selectedCountries, setSelectedCountries] = useState<MultiValue<SelectOption>>(
    parseSearchParams(searchParams.get(filterParams.countries)),
  );
  const [selectedYears, setSelectedYears] = useState<MultiValue<SelectOption>>(
    parseSearchParams(searchParams.get(filterParams.years)),
  );

  useEffect(() => {
    const params = new URLSearchParams();

    if (selectedGenres.length) {
      params.set(filterParams.genres, formatSelectedOptions(selectedGenres));
    }
    if (selectedCountries.length) {
      params.set(filterParams.countries, formatSelectedOptions(selectedCountries));
    }
    if (selectedYears.length) {
      params.set(filterParams.years, formatSelectedOptions(selectedYears));
    }

    if (params.toString() !== searchParams.toString()) {
      if (isNavigate && !searchParams.has("code")) {
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
  };
};
