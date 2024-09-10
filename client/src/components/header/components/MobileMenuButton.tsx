import React from "react";
import { Menu } from "lucide-react";
import MobileMenu from "./MobileMenu.tsx";
import useMobileMenu from "../../../hooks/useMobileMenu.tsx";

const MobileMenuButton: React.FC = () => {
  const { showMobileMenu, toggleMobileMenu } = useMobileMenu();

  return (
    <>
      <button
        className={`relative hover:text-teal-300 lg:hidden ${showMobileMenu ? "text-teal-300" : "text-white"}`}
        onClick={toggleMobileMenu}
      >
        <Menu size={32} />
      </button>
      {showMobileMenu && <MobileMenu toggleMobileMenu={toggleMobileMenu} />}
    </>
  );
};

export default MobileMenuButton;
