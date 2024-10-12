import type { FC } from "react";
import { Search } from "lucide-react";

interface MobileSearchButtonProps {
  showMobileSearch: boolean;
  toggleMobileSearch: () => void;
}

export const MobileSearchButton: FC<MobileSearchButtonProps> = ({ showMobileSearch, toggleMobileSearch }) => {
  return (
    <button
      className={`hover:text-cyan-300 ${showMobileSearch ? "text-cyan-300" : "text-white"}`}
      onClick={toggleMobileSearch}
    >
      <Search size={32} />
    </button>
  );
};
