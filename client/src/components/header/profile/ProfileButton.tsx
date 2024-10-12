import type { FC } from "react";
import { CircleUserRound } from "lucide-react";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";

export const ProfileButton: FC = () => {
  const { t } = useTranslation();

  return (
    <Link to={"/profile"} className="inline-flex items-center gap-3 hover:text-cyan-300">
      <CircleUserRound size={22} />
      {t("components.header.profile.profile")}
    </Link>
  );
};
