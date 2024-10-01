import type { FC } from "react";
import { LogOut } from "lucide-react";
import { useAuth0 } from "@auth0/auth0-react";

const LogoutButton: FC = () => {
  const { logout } = useAuth0();

  return (
    <button
      className="p inline-flex items-center gap-3 text-red-500 hover:text-red-300"
      onClick={() => logout({ logoutParams: { returnTo: window.location.origin } })}
    >
      <LogOut size={22} />
      Logout
    </button>
  );
};

export default LogoutButton;
