import type { FC } from "react";
import { Link } from "react-router-dom";
import logo from "@/assets/icons/logo.png";

export const Logo: FC = () => {
  return (
    <Link to="/">
      <img src={logo} alt="logo" className="w-44" />
    </Link>
  );
};
