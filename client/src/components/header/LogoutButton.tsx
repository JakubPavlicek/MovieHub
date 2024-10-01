import type { FC } from "react";
import { LogOut } from "lucide-react";
import { useAuth0 } from "@auth0/auth0-react";

const LogoutButton: FC = () => {
  const { logout } = useAuth0();

  return (
    <button
      className="inline-flex items-center hover:text-cyan-300"
      onClick={() => logout({ logoutParams: { returnTo: window.location.origin } })}
    >
      <LogOut size={22} className="lg:mr-3" />
      Logout
    </button>
  );
};

export default LogoutButton;
