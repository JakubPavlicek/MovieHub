import type { FC } from "react";
import { ChevronLeft } from "lucide-react";
import { Link } from "react-router-dom";
import useGenres from "@/hooks/useGenres.ts";
import useCountries from "@/hooks/useCountries.ts";
import MobileDropdownMenu from "./MobileDropdownMenu.tsx";

interface MobileMenuProps {
  toggleMobileMenu: () => void;
}

const MobileMenu: FC<MobileMenuProps> = ({ toggleMobileMenu }) => {
  const { genres } = useGenres();
  const { countries } = useCountries();

  return (
    <nav className="absolute left-5 top-20 z-10 flex w-72 flex-col items-start rounded-md bg-neutral-800 px-4 py-3">
      <div className="inline-flex -translate-x-1.5 items-center justify-start text-gray-400">
        <button className="inline-flex items-center hover:text-gray-300" onClick={toggleMobileMenu}>
          <ChevronLeft size={24} />
          Close menu
        </button>
      </div>
      <Link to="/" className="w-full py-3 hover:text-teal-300">
        Home
      </Link>
      <hr className="w-full border-neutral-700" />
      <MobileDropdownMenu title="Genres" type="genre" items={genres} />
      <hr className="w-full border-neutral-700" />
      <MobileDropdownMenu title="Countries" type="country" items={countries} />
    </nav>
  );
};

export default MobileMenu;
