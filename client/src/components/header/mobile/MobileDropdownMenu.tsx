import type { FC } from "react";
import { MobileDropdownButton } from "@/components/header/mobile/MobileDropdownButton";
import { MobileDropdownItems } from "@/components/header/mobile/MobileDropdownItems";
import { useDropdownMenu } from "@/hooks/useDropdownMenu";
import type { components } from "@/api/api";

interface MobileMenuDropdownProps {
  title: string;
  type: "genre" | "country";
  items: components["schemas"]["GenreDetailsResponse"][] | components["schemas"]["CountryDetailsResponse"][];
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
