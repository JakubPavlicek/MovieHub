import type { FC } from "react";
import { CategoryItem, CategoryItemType } from "@/types/category.ts";
import MobileDropdownButton from "./MobileDropdownButton.tsx";
import MobileDropdownItems from "./MobileDropdownItems.tsx";
import useDropdownMenu from "@/hooks/useDropdownMenu.ts";

interface MobileMenuDropdownProps {
  title: "Genres" | "Countries";
  type: CategoryItemType;
  items: CategoryItem[];
}

const MobileDropdownMenu: FC<MobileMenuDropdownProps> = ({ title, type, items }) => {
  const { isOpen, toggleDropdown } = useDropdownMenu();

  return (
    <div className="w-full">
      <MobileDropdownButton isOpen={isOpen} title={title} toggleDropdown={toggleDropdown} />
      <MobileDropdownItems isOpen={isOpen} type={type} items={items} />
    </div>
  );
};

export default MobileDropdownMenu;
