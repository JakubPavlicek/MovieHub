import type { FC } from "react";
import { DropdownItem } from "./DropdownMenu.tsx";

interface MobileDropdownItemsProps {
  isOpen: boolean;
  items: DropdownItem[];
}

const MobileDropdownItems: FC<MobileDropdownItemsProps> = ({ isOpen, items }) => {
  return (
    <div className={`grid w-full transition-all duration-300 ${isOpen ? "grid-rows-[1fr]" : "grid-rows-[0fr]"}`}>
      <div className="grid grid-cols-2 overflow-hidden">
        {items.map((item) => (
          <button key={item.id} className="min-h-10 truncate py-2.5 text-left text-sm text-gray-400 hover:text-white">
            {item.name}
          </button>
        ))}
      </div>
    </div>
  );
};

export default MobileDropdownItems;
