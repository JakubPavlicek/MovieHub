import { NavLink } from "react-router-dom";
import logo from "../../../assets/logo.png";
import React from "react";

const Logo: React.FC = () => {
  return (
    <NavLink to="/">
      <img src={logo} alt="logo" className="w-[180px]" />
    </NavLink>
  );
};

export default Logo;
