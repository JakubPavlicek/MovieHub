import type { FC } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import { CircleUserRound } from "lucide-react";
import { useTranslation } from "react-i18next";

export const LoginButton: FC = () => {
  const { loginWithRedirect } = useAuth0();
  const { t } = useTranslation();

  return (
    <button className="inline-flex min-w-fit items-center hover:text-cyan-300" onClick={() => loginWithRedirect()}>
      <CircleUserRound size={32} className="shrink-0 md:mr-3" />
      <span className="hidden md:block">{t("components.header.desktop.login")}</span>
    </button>
  );
};
