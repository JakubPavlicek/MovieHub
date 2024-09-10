import React from "react";
import { ChevronDown, ChevronUp } from "lucide-react";

interface MobileDropdownButtonProps {
  title: string;
  isOpen: boolean;
  toggleDropdown: () => void;
}

const MobileDropdownButton: React.FC<MobileDropdownButtonProps> = ({ title, isOpen, toggleDropdown }) => {
  return (
    <button className="inline-flex w-full justify-between py-3 hover:text-teal-300" onClick={toggleDropdown}>
      {title}
      {isOpen ? <ChevronUp size={24} /> : <ChevronDown size={24} />}
    </button>
  );
};

export default MobileDropdownButton;
