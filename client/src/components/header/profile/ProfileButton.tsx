import type { FC } from "react";
import { CircleUserRound } from "lucide-react";
import { Link } from "react-router-dom";

const ProfileButton: FC = () => {
  return (
    <Link to={"/profile"} className="inline-flex items-center gap-3 hover:text-cyan-300">
      <CircleUserRound size={22} />
      Profile
    </Link>
  );
};

export default ProfileButton;
