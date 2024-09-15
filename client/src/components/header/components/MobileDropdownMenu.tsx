import type { FC } from "react";
import { DropdownItem } from "./DropdownMenu.tsx";
import MobileDropdownButton from "./MobileDropdownButton.tsx";
import MobileDropdownItems from "./MobileDropdownItems.tsx";
import useDropdownMenu from "../../../hooks/useDropdownMenu.ts";

interface MobileMenuDropdownProps {
  title: string;
  items: DropdownItem[];
}

const MobileDropdownMenu: FC<MobileMenuDropdownProps> = ({ title, items }) => {
  const { isOpen, toggleDropdown } = useDropdownMenu();

  return (
    <div className="w-full">
      <MobileDropdownButton title={title} isOpen={isOpen} toggleDropdown={toggleDropdown} />
      <MobileDropdownItems isOpen={isOpen} items={items} />
    </div>
  );
};

export default MobileDropdownMenu;
