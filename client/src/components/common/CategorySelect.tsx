import { type FC, useCallback, useMemo } from "react";
import { selectStyles } from "@/styles/selectStyles";
import Select, { MultiValue } from "react-select";
import type { components } from "@/api/api";
import type { SelectOption } from "@/types/selectOption";

interface CategorySelectProps {
  placeholder: string;
  items: components["schemas"]["GenreDetailsResponse"][] | components["schemas"]["CountryDetailsResponse"][];
  selectedItems: MultiValue<SelectOption>;
  setSelectedItems: (genres: MultiValue<SelectOption>) => void;
}

export const CategorySelect: FC<CategorySelectProps> = ({ placeholder, items, selectedItems, setSelectedItems }) => {
  const handleChange = useCallback(
    (selectedOptions: MultiValue<SelectOption>) => setSelectedItems(selectedOptions),
    [setSelectedItems],
  );

  const options = useMemo(() => {
    return items.map((item) => ({
      value: item.name,
      label: item.name,
    }));
  }, [items]);

  return (
    <Select
      isMulti
      placeholder={placeholder}
      options={options}
      className="basic-multi-select"
      classNamePrefix="select"
      styles={selectStyles}
      value={selectedItems}
      onChange={handleChange}
      closeMenuOnSelect={false}
    />
  );
};
