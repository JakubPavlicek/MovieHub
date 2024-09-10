import React from "react";
import { CircleUserRound } from "lucide-react";
import { useAuth0 } from "@auth0/auth0-react";

const LoginButton: React.FC = () => {
  const { loginWithRedirect } = useAuth0();

  return (
    <button className="inline-flex items-center hover:text-teal-300" onClick={() => loginWithRedirect()}>
      <CircleUserRound size={32} className="lg:mr-3" />
      <span className="hidden lg:block">Login</span>
    </button>
  );
};

export default LoginButton;
