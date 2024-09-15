import type { FC } from "react";
import { NavLink } from "react-router-dom";
import logo from "../../../assets/logo.png";

const Logo: FC = () => {
  return (
    <NavLink to="/">
      <img src={logo} alt="logo" className="w-44" />
    </NavLink>
  );
};

export default Logo;
