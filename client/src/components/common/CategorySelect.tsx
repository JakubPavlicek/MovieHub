import { type FC, useCallback, useMemo } from "react";
import { selectStyles } from "@/styles/selectStyles";
import Select, { MultiValue } from "react-select";
import type { components } from "@/api/api";
import type { SelectOption } from "@/types/selectOption";
import { useTranslation } from "react-i18next";

interface CategorySelectProps {
  placeholder: string;
  type: "genres" | "countries";
  items: components["schemas"]["GenreDetailsResponse"][] | components["schemas"]["CountryDetailsResponse"][];
  selectedItems: MultiValue<SelectOption>;
  setSelectedItems: (genres: MultiValue<SelectOption>) => void;
}

export const CategorySelect: FC<CategorySelectProps> = ({
  placeholder,
  type,
  items,
  selectedItems,
  setSelectedItems,
}) => {
  const { t } = useTranslation();
  const handleChange = useCallback(
    (selectedOptions: MultiValue<SelectOption>) => setSelectedItems(selectedOptions),
    [setSelectedItems],
  );

  const options = useMemo(() => {
    return items.map((item) => ({
      value: item.name,
      label: t(`${type === "genres" ? "genres" : "countries"}.${item.name}.single`),
    }));
  }, [items, type, t]);

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
