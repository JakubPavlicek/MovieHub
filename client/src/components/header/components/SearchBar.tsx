import type { FC } from "react";
import { Search } from "lucide-react";

export interface SearchBarProps {
  autoFocus: boolean;
}

const SearchBar: FC<SearchBarProps> = ({ autoFocus }) => {
  return (
    <div className="relative text-white">
      <div className="absolute bottom-0 left-3 top-1/2 -translate-y-1/2">
        <button className="hover:text-teal-300">
          <Search size={21} />
        </button>
      </div>
      <input
        type="search"
        autoFocus={autoFocus}
        placeholder="Enter keywords..."
        className="w-full rounded-3xl border-2 border-transparent bg-gray-400 bg-opacity-50 py-2 pl-10 pr-4 focus:border-teal-300 focus:outline-none"
      />
    </div>
  );
};

export default SearchBar;
