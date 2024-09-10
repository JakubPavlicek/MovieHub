import { NavLink } from "react-router-dom";
import React from "react";
import useGenres from "../../../hooks/useGenres.tsx";
import DropdownMenu from "./DropdownMenu.tsx";
import useCountries from "../../../hooks/useCountries.tsx";

const NavMenu: React.FC = () => {
  const { genres } = useGenres();
  const { countries } = useCountries();

  return (
    <nav className="hidden items-center justify-center gap-5 lg:flex">
      <NavLink to="/" className="p-3 hover:text-teal-300">
        Home
      </NavLink>
      <DropdownMenu title="Genre" items={genres} />
      <DropdownMenu title="Country" items={countries} />
    </nav>
  );
};

export default NavMenu;
