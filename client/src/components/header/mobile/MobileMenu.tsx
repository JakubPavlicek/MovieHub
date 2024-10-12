import type { FC } from "react";
import { ChevronLeft } from "lucide-react";
import { Link } from "react-router-dom";
import { useGenres } from "@/hooks/useGenres";
import { useCountries } from "@/hooks/useCountries";
import { MobileDropdownMenu } from "@/components/header/mobile/MobileDropdownMenu";
import { MobileLanguageDropdown } from "@/components/header/mobile/MobileLanguageDropdown";
import { useTranslation } from "react-i18next";

interface MobileMenuProps {
  toggleMobileMenu: () => void;
}

export const MobileMenu: FC<MobileMenuProps> = ({ toggleMobileMenu }) => {
  const { t } = useTranslation();
  const { genres } = useGenres();
  const { countries } = useCountries();

  return (
    <nav className="absolute left-5 top-20 z-10 flex w-72 flex-col items-start rounded-md bg-neutral-800 px-4 py-3">
      <div className="inline-flex -translate-x-1.5 items-center justify-start text-gray-400">
        <button className="inline-flex items-center hover:text-gray-300" onClick={toggleMobileMenu}>
          <ChevronLeft size={24} />
          {t("components.header.mobile.closeMenu")}
        </button>
      </div>
      <Link to="/" className="w-full py-3 hover:text-cyan-300">
        {t("components.header.mobile.home")}
      </Link>
      <hr className="w-full border-neutral-700" />
      <MobileDropdownMenu title="Genres" type="genre" items={genres} />
      <hr className="w-full border-neutral-700" />
      <MobileDropdownMenu title="Countries" type="country" items={countries} />
      <hr className="w-full border-neutral-700" />
      <MobileLanguageDropdown />
    </nav>
  );
};
