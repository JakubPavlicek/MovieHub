import type { FC } from "react";
import { DropdownItem } from "@/types/navigation.ts";

interface DropdownProps {
  title: string;
  items: DropdownItem[];
}

const DropdownMenu: FC<DropdownProps> = ({ title, items }) => {
  return (
    <div className="group relative">
      <button className="p-3 group-hover:text-teal-300">{title}</button>
      <div className="absolute left-0 top-12 z-10 hidden rounded-md bg-gray-800 p-3 group-hover:block">
        <div className="grid grid-cols-[repeat(3,minmax(130px,1fr))]">
          {items.map((item) => (
            <button
              id={item.id}
              key={item.id}
              className="w-full truncate rounded-md px-3 py-1 text-left hover:bg-gray-950 hover:text-teal-300"
            >
              {item.name}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

export default DropdownMenu;
