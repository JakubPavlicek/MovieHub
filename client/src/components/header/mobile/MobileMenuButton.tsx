import type { FC } from "react";
import { Menu } from "lucide-react";

interface MobileMenuButtonProps {
  showMobileMenu: boolean;
  toggleMobileMenu: () => void;
}

export const MobileMenuButton: FC<MobileMenuButtonProps> = ({ showMobileMenu, toggleMobileMenu }) => {
  return (
    <button
      className={`hover:text-cyan-300 ${showMobileMenu ? "text-cyan-300" : "text-white"}`}
      onClick={toggleMobileMenu}
    >
      <Menu size={32} />
    </button>
  );
};
