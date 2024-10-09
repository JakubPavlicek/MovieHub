import type { SelectOption } from "@/types/selectOption";
import type { GroupBase, StylesConfig } from "react-select";

export const selectStyles: StylesConfig<SelectOption, true, GroupBase<SelectOption>> = {
  control: (provided) => ({
    ...provided,
    minWidth: "200px", // Control min width
    backgroundColor: "#1a1a1a", // Control background color
    borderColor: "#444", // Control border color
    boxShadow: "none", // Remove shadow
    "&:hover": {
      borderColor: "#888", // Border color on hover
    },
  }),
  placeholder: (provided) => ({
    ...provided,
    color: "lightgray", // Change placeholder text color
  }),
  menu: (provided) => ({
    ...provided,
    backgroundColor: "#1a1a1a", // Control background color
  }),
  option: (provided, state) => ({
    ...provided,
    backgroundColor: state.isFocused ? "#333" : "#1a1a1a", // Option background color
    color: state.isFocused ? "#fff" : "#ccc", // Option text color
    "&:active": {
      backgroundColor: "#555", // Active option background color
    },
  }),
  multiValue: (provided) => ({
    ...provided,
    backgroundColor: "#444", // Background color for selected options
  }),
  multiValueLabel: (provided) => ({
    ...provided,
    color: "#fff", // Text color for selected options
  }),
  multiValueRemove: (provided) => ({
    ...provided,
    color: "#fff", // Color for the remove button
    padding: "0 8px", // Add padding to make the "X" button wider
    ":hover": {
      backgroundColor: "#888", // Remove button hover color
      color: "#fff", // Remove button text color on hover
    },
  }),
};
