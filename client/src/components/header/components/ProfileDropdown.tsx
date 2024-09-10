import React from "react";
import { ChevronDown } from "lucide-react";
import { useAuth0 } from "@auth0/auth0-react";

const ProfileDropdown: React.FC = () => {
  const { user } = useAuth0();

  return (
    <button className="inline-flex items-center justify-between gap-1 rounded-lg border-2 border-gray-400 p-2">
      <span className="hidden truncate lg:block lg:max-w-36">{user?.nickname}</span>
      <ChevronDown size={20} />
    </button>
  );
};

export default ProfileDropdown;
