import type { FC } from "react";
import { Search } from "lucide-react";

interface MobileSearchButtonProps {
  showMobileSearch: boolean;
  toggleMobileSearch: () => void;
}

const MobileSearchButton: FC<MobileSearchButtonProps> = ({ showMobileSearch, toggleMobileSearch }) => {
  return (
    <button
      className={`hover:text-teal-300 ${showMobileSearch ? "text-teal-300" : "text-white"}`}
      onClick={toggleMobileSearch}
    >
      <Search size={32} />
    </button>
  );
};

export default MobileSearchButton;
