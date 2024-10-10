import type { FC } from "react";
import { YearSelect } from "@/components/common/YearSelect";
import { CategorySelect } from "@/components/common/CategorySelect";
import { useCountries } from "@/hooks/useCountries";
import { useGenres } from "@/hooks/useGenres";
import { useYears } from "@/hooks/useYears";
import { useFilterSelects } from "@/hooks/useFilterSelects";

interface FilterSelectsProps {
  showFilters: boolean;
  enableNavigate: boolean;
}

export const FilterSelects: FC<FilterSelectsProps> = ({ showFilters, enableNavigate }) => {
  const { genres } = useGenres();
  const { countries } = useCountries();
  const { years } = useYears();

  const {
    selectedGenres,
    setSelectedGenres,
    selectedCountries,
    setSelectedCountries,
    selectedYears,
    setSelectedYears,
  } = useFilterSelects(enableNavigate);

  return (
    <div
      className={`grid text-white transition-all duration-300 ${
        showFilters ? "mb-8 mt-2 max-h-[200px] opacity-100" : "max-h-0 opacity-0"
      }`}
    >
      <div className="flex flex-row flex-wrap items-center justify-center gap-4 sm:justify-start">
        <CategorySelect
          placeholder="Select genres"
          items={genres}
          selectedItems={selectedGenres}
          setSelectedItems={setSelectedGenres}
        />
        <CategorySelect
          placeholder="Select countries"
          items={countries}
          selectedItems={selectedCountries}
          setSelectedItems={setSelectedCountries}
        />
        <YearSelect
          placeholder="Select years"
          years={years}
          selectedYears={selectedYears}
          setSelectedYears={setSelectedYears}
        />
      </div>
    </div>
  );
};
