import type { FC } from "react";
import { Link } from "react-router-dom";
import logo from "@/assets/logo.png";

const Logo: FC = () => {
  return (
    <Link to="/">
      <img src={logo} alt="logo" className="w-44" />
    </Link>
  );
};

export default Logo;
