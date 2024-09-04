import React from "react";
import { Search } from "lucide-react";

interface SearchBarProps {
  autoFocus: boolean;
}

const SearchBar: React.FC<SearchBarProps> = ({ autoFocus }) => {
  return (
    <div className="relative">
      <div className="absolute inset-y-0 left-3 flex items-center">
        <button className="text-white hover:text-teal-300">
          <Search size={20} />
        </button>
      </div>
      <input
        type="search"
        autoFocus={autoFocus}
        placeholder="Enter keywords..."
        className="block w-full rounded-3xl border-2 border-transparent bg-gray-400 bg-opacity-50 p-2 pl-10 pr-4 text-white focus:border-teal-300 focus:outline-none"
      />
    </div>
  );
};

export default SearchBar;
