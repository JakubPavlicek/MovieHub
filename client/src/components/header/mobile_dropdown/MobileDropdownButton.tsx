import type { FC } from "react";
import { ChevronDown, ChevronUp } from "lucide-react";

interface MobileDropdownButtonProps {
  title: string;
  isOpen: boolean;
  toggleDropdown: () => void;
}

const MobileDropdownButton: FC<MobileDropdownButtonProps> = ({ title, isOpen, toggleDropdown }) => {
  return (
    <button className="inline-flex w-full justify-between py-3 hover:text-cyan-300" onClick={toggleDropdown}>
      {title}
      {isOpen ? <ChevronUp size={24} /> : <ChevronDown size={24} />}
    </button>
  );
};

export default MobileDropdownButton;
