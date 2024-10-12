import type { FC } from "react";
import { Link } from "react-router-dom";
import { useGenres } from "@/hooks/useGenres";
import { useCountries } from "@/hooks/useCountries";
import { DropdownMenu } from "@/components/header/desktop/DropdownMenu";
import { useTranslation } from "react-i18next";

export const NavMenu: FC = () => {
  const { t } = useTranslation();
  const { genres } = useGenres();
  const { countries } = useCountries();

  return (
    <div className="flex items-center justify-center xl:gap-3">
      <Link to="/movies" className="p-3 hover:text-cyan-300">
        {t("components.header.desktop.home")}
      </Link>
      <DropdownMenu title={t("components.header.desktop.genre")} type="genre" items={genres} />
      <DropdownMenu title={t("components.header.desktop.country")} type="country" items={countries} />
    </div>
  );
};
