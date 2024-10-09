import type { FC } from "react";
import { Filter } from "lucide-react";

interface FilterButtonProps {
  toggleFilters: () => void;
}

export const FilterButton: FC<FilterButtonProps> = ({ toggleFilters }) => {
  return (
    <button
      className="mb-3 inline-flex items-center gap-2 rounded-md bg-gray-700 px-2 py-1 text-gray-200 hover:bg-gray-600"
      onClick={toggleFilters}
    >
      <Filter size={18} />
      Filter
    </button>
  );
};
