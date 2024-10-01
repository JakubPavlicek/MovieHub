import type { FC } from "react";
import { Link } from "react-router-dom";
import { Genre } from "@/types/genre.ts";
import { Country } from "@/types/country.ts";

interface MobileDropdownItemsProps {
  isOpen: boolean;
  type: "genre" | "country";
  items: Genre[] | Country[];
}

const MobileDropdownItems: FC<MobileDropdownItemsProps> = ({ isOpen, type, items }) => {
  return (
    <div
      className={`grid w-full transition-all duration-300 ${isOpen ? "grid-rows-[1fr]" : "grid-rows-[0fr]"}`}
    >
      <div className="grid grid-cols-2 overflow-hidden">
        {items.map((item) => (
          <Link
            to={`/${type}/${item.name.toLowerCase()}`}
            key={item.id}
            className="min-h-10 truncate py-2.5 text-left text-sm text-gray-400 hover:text-white"
          >
            {item.name}
          </Link>
        ))}
      </div>
    </div>
  );
};

export default MobileDropdownItems;