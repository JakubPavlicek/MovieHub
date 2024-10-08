import { type FC } from "react";
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
  const handleChange = (selectedOptions: MultiValue<SelectOption>) => {
    setSelectedYears(selectedOptions);
    console.log(selectedOptions); // Log the selected options to see them
  };

  const options = years.map((year) => ({
    value: year.toString(),
    label: year.toString(),
  }));

  const customStyles = {
    control: (provided: any) => ({
      ...provided,
      minWidth: "200px",
      backgroundColor: "#1a1a1a", // Control background color
      borderColor: "#444", // Control border color
      boxShadow: "none", // Remove shadow
      "&:hover": {
        borderColor: "#888", // Border color on hover
      },
    }),
    placeholder: (provided: any) => ({
      ...provided,
      color: "lightgray", // Change placeholder text color
    }),
    menu: (provided: any) => ({
      ...provided,
      backgroundColor: "#1a1a1a", // Control background color
    }),
    option: (provided: any, state: any) => ({
      ...provided,
      backgroundColor: state.isFocused ? "#333" : "#1a1a1a", // Option background color
      color: state.isFocused ? "#fff" : "#ccc", // Option text color
      "&:active": {
        backgroundColor: "#555", // Active option background color
      },
    }),
    multiValue: (provided: any) => ({
      ...provided,
      backgroundColor: "#444", // Background color for selected options
    }),
    multiValueLabel: (provided: any) => ({
      ...provided,
      color: "#fff", // Text color for selected options
    }),
    multiValueRemove: (provided: any) => ({
      ...provided,
      color: "#fff", // Color for the remove button
      ":hover": {
        backgroundColor: "#888", // Remove button hover color
        color: "#fff", // Remove button text color on hover
      },
    }),
  };

  return (
    <Select
      isMulti
      placeholder={placeholder}
      options={options}
      className="basic-multi-select"
      classNamePrefix="select"
      styles={customStyles}
      value={selectedYears}
      onChange={handleChange}
    />
  );
};
