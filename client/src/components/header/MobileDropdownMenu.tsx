import type { FC } from "react";
import MobileDropdownButton from "./MobileDropdownButton.tsx";
import MobileDropdownItems from "./MobileDropdownItems.tsx";
import useDropdownMenu from "@/hooks/useDropdownMenu.ts";
import { Genre } from "@/types/genre.ts";
import { Country } from "@/types/country.ts";

interface MobileMenuDropdownProps {
  title: "Genres" | "Countries";
  type: "genre" | "country";
  items: Genre[] | Country[];
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
