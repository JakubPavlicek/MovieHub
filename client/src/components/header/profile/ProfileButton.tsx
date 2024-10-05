import type { FC } from "react";
import { CircleUserRound } from "lucide-react";

const ProfileButton: FC = () => {
  return (
    <button className="inline-flex items-center gap-3 hover:text-cyan-300">
      <CircleUserRound size={22} />
      Profile
    </button>
  );
};

export default ProfileButton;
