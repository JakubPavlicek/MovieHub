import type { FC } from "react";
import { MobileDropdownButton } from "@/components/header/mobile_dropdown/MobileDropdownButton";
import { MobileDropdownItems } from "@/components/header/mobile_dropdown/MobileDropdownItems";
import { useDropdownMenu } from "@/hooks/useDropdownMenu";
import type { Genre } from "@/types/genre";
import type { Country } from "@/types/country";

interface MobileMenuDropdownProps {
  title: "Genres" | "Countries";
  type: "genre" | "country";
  items: Genre[] | Country[];
}

export const MobileDropdownMenu: FC<MobileMenuDropdownProps> = ({ title, type, items }) => {
  const { isOpen, toggleDropdown } = useDropdownMenu();

  return (
    <div className="w-full">
      <MobileDropdownButton isOpen={isOpen} title={title} toggleDropdown={toggleDropdown} />
      <MobileDropdownItems isOpen={isOpen} type={type} items={items} />
    </div>
  );
};
