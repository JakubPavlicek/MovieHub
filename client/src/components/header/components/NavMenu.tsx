import type { FC } from "react";
import { NavLink } from "react-router-dom";
import DropdownMenu from "./DropdownMenu.tsx";
import useGenres from "../../../hooks/useGenres.ts";
import useCountries from "../../../hooks/useCountries.ts";

const NavMenu: FC = () => {
  const { genres } = useGenres();
  const { countries } = useCountries();

  return (
    <nav className="flex items-center justify-center xl:gap-3">
      <NavLink to="/" className="p-3 hover:text-teal-300">
        Home
      </NavLink>
      <DropdownMenu title="Genre" items={genres} />
      <DropdownMenu title="Country" items={countries} />
    </nav>
  );
};

export default NavMenu;
