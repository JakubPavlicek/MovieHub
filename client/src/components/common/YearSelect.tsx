import { type FC, useCallback, useMemo } from "react";
import { selectStyles } from "@/styles/selectStyles";
import Select, { MultiValue } from "react-select";
import type { components } from "@/api/api";
import type { SelectOption } from "@/types/selectOption";

interface YearSelectProps {
  placeholder: string;
  years: components["schemas"]["YearListResponse"]["years"];
  selectedYears: MultiValue<SelectOption>;
  setSelectedYears: (genres: MultiValue<SelectOption>) => void;
}

export const YearSelect: FC<YearSelectProps> = ({ placeholder, years, selectedYears, setSelectedYears }) => {
  const handleChange = useCallback(
    (selectedOptions: MultiValue<SelectOption>) => setSelectedYears(selectedOptions),
    [setSelectedYears],
  );

  const options = useMemo(() => {
    return years.map((year) => ({
      value: year.toString(),
      label: year.toString(),
    }));
  }, [years]);

  return (
    <Select
      isMulti
      placeholder={placeholder}
      options={options}
      className="basic-multi-select"
      classNamePrefix="select"
      styles={selectStyles}
      value={selectedYears}
      onChange={handleChange}
      closeMenuOnSelect={false}
    />
  );
};
