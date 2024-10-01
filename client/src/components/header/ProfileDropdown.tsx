import { type FC, useState } from "react";
import { ChevronDown, ChevronUp, CircleUserRound } from "lucide-react";
import { useAuth0 } from "@auth0/auth0-react";
import LogoutButton from "@/components/header/LogoutButton";

const ProfileDropdown: FC = () => {
  const [showDropdown, setShowDropdown] = useState<boolean>(false);
  const { user } = useAuth0();

  // TODO: fix styles

  return (
    <div className="relative">
      <button
        className="inline-flex items-center justify-between gap-1 rounded-lg border-2 border-gray-400 p-2"
        onClick={() => setShowDropdown(!showDropdown)}
      >
        <span className="hidden truncate lg:block lg:max-w-36">{user?.nickname}</span>
        {showDropdown ? <ChevronUp size={20} /> : <ChevronDown size={20} />}
      </button>
      {showDropdown && (
        <div className="absolute right-0 top-12 flex flex-col gap-2 bg-gray-500 p-2">
          <button className="inline-flex items-center gap-3 hover:text-cyan-300">
            <CircleUserRound size={22} />
            Profile
          </button>
          <LogoutButton />
        </div>
      )}
    </div>
  );
};

export default ProfileDropdown;
