import React from "react";
import { Menu } from "lucide-react";

const MobileMenuButton: React.FC = () => {
  return (
    <button className="hover:text-teal-300 lg:hidden">
      <Menu size={32} />
    </button>
  );
};

export default MobileMenuButton;
