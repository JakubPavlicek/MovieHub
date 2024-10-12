import type { FC } from "react";
import { Link } from "react-router-dom";
import { useGenres } from "@/hooks/useGenres";
import { useCountries } from "@/hooks/useCountries";
import { DropdownMenu } from "@/components/header/DropdownMenu";
import { useTranslation } from "react-i18next";

export const NavMenu: FC = () => {
  const { genres } = useGenres();
  const { countries } = useCountries();
  const { t } = useTranslation();

  return (
    <div className="flex items-center justify-center xl:gap-3">
      <Link to="/movies" className="p-3 hover:text-cyan-300">
        {t("home")}
      </Link>
      <DropdownMenu title="Genre" type="genre" items={genres} />
      <DropdownMenu title="Country" type="country" items={countries} />
    </div>
  );
};
