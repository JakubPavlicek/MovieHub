import type { FC } from "react";
import { Link } from "react-router-dom";
import DropdownMenu from "./DropdownMenu.tsx";
import useGenres from "@/hooks/useGenres.ts";
import useCountries from "@/hooks/useCountries.ts";

const NavMenu: FC = () => {
  const { genres } = useGenres();
  const { countries } = useCountries();

  return (
    <div className="flex items-center justify-center xl:gap-3">
      <Link to="/" className="p-3 hover:text-teal-300">
        Home
      </Link>
      <DropdownMenu title="Genre" type="genre" items={genres} />
      <DropdownMenu title="Country" type="country" items={countries} />
    </div>
  );
};

export default NavMenu;
