import React from "react";
import { Menu } from "lucide-react";

interface MobileMenuButtonProps {
  showMobileMenu: boolean;
  toggleMobileMenu: () => void;
}

const MobileMenuButton: React.FC<MobileMenuButtonProps> = ({ showMobileMenu, toggleMobileMenu }) => {
  return (
    <button
      className={`hover:text-teal-300 ${showMobileMenu ? "text-teal-300" : "text-white"}`}
      onClick={toggleMobileMenu}
    >
      <Menu size={32} />
    </button>
  );
};

export default MobileMenuButton;
