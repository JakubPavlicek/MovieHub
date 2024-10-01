import type { FC } from "react";
import { Link } from "react-router-dom";
import { Genre } from "@/types/genre.ts";
import { Country } from "@/types/country.ts";

interface DropdownProps {
  title: "Genre" | "Country";
  type: "genre" | "country";
  items: Genre[] | Country[];
}

const DropdownMenu: FC<DropdownProps> = ({ title, type, items }) => {
  return (
    <div className="group relative">
      <button className="p-3 group-hover:text-cyan-300">{title}</button>
      <div className="absolute left-0 top-12 z-10 hidden rounded-md bg-gray-800 p-3 group-hover:block">
        <div className="grid grid-cols-[repeat(3,minmax(130px,1fr))]">
          {items.map((item) => (
            <Link
              to={`/${type}/${item.name.toLowerCase()}`}
              id={item.id}
              key={item.id}
              className="w-full truncate rounded-md px-3 py-1 text-left hover:bg-gray-950 hover:text-cyan-300"
            >
              {item.name}
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
};

export default DropdownMenu;
