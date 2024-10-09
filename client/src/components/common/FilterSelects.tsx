import type { FC } from "react";
import type { MultiValue } from "react-select";
import type { SelectOption } from "@/types/selectOption";
import { YearSelect } from "@/components/common/YearSelect";
import { CategorySelect } from "@/components/common/CategorySelect";
import { useCountries } from "@/hooks/useCountries";
import { useGenres } from "@/hooks/useGenres";
import { useYears } from "@/hooks/useYears";

interface FilterSelectsProps {
  selectedGenres: MultiValue<SelectOption>;
  setSelectedGenres: (selected: MultiValue<SelectOption>) => void;
  selectedCountries: MultiValue<SelectOption>;
  setSelectedCountries: (selected: MultiValue<SelectOption>) => void;
  selectedYears: MultiValue<SelectOption>;
  setSelectedYears: (selected: MultiValue<SelectOption>) => void;
  showFilters: boolean;
}

export const FilterSelects: FC<FilterSelectsProps> = ({
  selectedGenres,
  setSelectedGenres,
  selectedCountries,
  setSelectedCountries,
  selectedYears,
  setSelectedYears,
  showFilters,
}) => {
  const { genres } = useGenres();
  const { countries } = useCountries();
  const { years } = useYears();

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
