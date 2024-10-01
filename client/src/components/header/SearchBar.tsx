import React, { type FC, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Search } from "lucide-react";

export interface SearchBarProps {
  autoFocus: boolean;
}

const SearchBar: FC<SearchBarProps> = ({ autoFocus }) => {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState<string>("");
  const inputRef = useRef<HTMLInputElement>(null);

  function searchMovies() {
    if (searchQuery === "") {
      return;
    }
    navigate(`/search/${searchQuery}`);
    setSearchQuery("");
    inputRef.current?.blur();
  }

  function handleKeyDown(e: React.KeyboardEvent<HTMLInputElement>) {
    if (e.key === "Enter") {
      searchMovies();
    }
  }

  // TODO: add useDebounce to show dropdown menu below searchbar of all the movies fetched

  return (
    <div className="relative text-white">
      <div className="absolute bottom-0 left-3 top-1/2 -translate-y-1/2">
        <button className="hover:text-cyan-300" onClick={searchMovies}>
          <Search size={21} />
        </button>
      </div>
      <input
        type="search"
        autoFocus={autoFocus}
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
        onKeyDown={handleKeyDown}
        ref={inputRef}
        placeholder="Enter keywords..."
        className="w-full rounded-3xl border-2 border-transparent bg-gray-400 bg-opacity-50 py-2 pl-10 pr-4 focus:border-cyan-300 focus:outline-none"
      />
    </div>
  );
};

export default SearchBar;
