import { NavLink } from "react-router-dom";
import React from "react";

const NavMenu: React.FC = () => {
  return (
    <nav className="hidden items-center justify-center gap-7 lg:flex">
      <NavLink to="/" className="p-2 hover:text-teal-300">
        Home
      </NavLink>
      <NavLink to="/" className="p-2 hover:text-teal-300">
        Genre
      </NavLink>
      <NavLink to="/" className="p-2 hover:text-teal-300">
        Country
      </NavLink>
    </nav>
  );
};

export default NavMenu;
