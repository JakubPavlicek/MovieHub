import type { FC } from "react";
import { LogOut } from "lucide-react";
import { useAuth0 } from "@auth0/auth0-react";
import { useTranslation } from "react-i18next";

export const LogoutButton: FC = () => {
  const { t } = useTranslation();
  const { logout } = useAuth0();

  return (
    <button
      className="inline-flex items-center gap-3 text-red-500 hover:text-red-400"
      onClick={() => logout({ logoutParams: { returnTo: window.location.origin } })}
    >
      <LogOut size={22} />
      {t("components.header.profile.logout")}
    </button>
  );
};
