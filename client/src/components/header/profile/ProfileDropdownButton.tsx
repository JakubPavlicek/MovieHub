import type { FC } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import { ChevronDown, ChevronUp } from "lucide-react";

interface ProfileDropdownButtonProps {
  showDropdown: boolean;
  setShowDropdown: (showDropdown: boolean) => void;
}

export const ProfileDropdownButton: FC<ProfileDropdownButtonProps> = ({ showDropdown, setShowDropdown }) => {
  const { user } = useAuth0();

  return (
    <button
      className="inline-flex items-center justify-between gap-1 rounded-lg border-2 border-gray-400 p-2 hover:border-gray-200"
      onClick={() => setShowDropdown(!showDropdown)}
    >
      <span className="hidden truncate md:block md:max-w-36">{user?.nickname}</span>
      {showDropdown ? <ChevronUp size={20} /> : <ChevronDown size={20} />}
    </button>
  );
};
