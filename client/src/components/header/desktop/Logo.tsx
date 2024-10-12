import { type FC, memo } from "react";
import { Link } from "react-router-dom";
import logo from "@/assets/icons/logo.png";

export const Logo: FC = memo(() => {
  return (
    <Link to="/movies">
      <img src={logo} alt="logo" className="w-44" />
    </Link>
  );
});
