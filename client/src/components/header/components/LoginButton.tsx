import React from "react";
import { CircleUserRound } from "lucide-react";

const LoginButton: React.FC = () => {
  return (
    <button className="inline-flex items-center hover:text-teal-300">
      <CircleUserRound size={32} className="lg:mr-3" />
      <span className="hidden lg:block">Login</span>
    </button>
  );
};

export default LoginButton;
