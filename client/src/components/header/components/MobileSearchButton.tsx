import React from "react";
import { Search } from "lucide-react";

interface MobileSearchButtonProps {
  isMobileSearch: boolean;
  toggleMobileSearch: () => void;
}

const MobileSearchButton: React.FC<MobileSearchButtonProps> = ({ isMobileSearch, toggleMobileSearch }) => {
  return (
    <button
      className={`hover:text-teal-300 sm:hidden ${isMobileSearch ? "text-teal-300" : "text-white"}`}
      onClick={toggleMobileSearch}
    >
      <Search size={32} />
    </button>
  );
};

export default MobileSearchButton;
