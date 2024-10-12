import { type FC, useState } from "react";
import { LogoutButton } from "@/components/header/profile/LogoutButton";
import { ProfileDropdownButton } from "@/components/header/profile/ProfileDropdownButton";
import { ProfileButton } from "@/components/header/profile/ProfileButton";

export const ProfileDropdown: FC = () => {
  const [showDropdown, setShowDropdown] = useState(false);

  return (
    <div className="relative">
      <ProfileDropdownButton showDropdown={showDropdown} setShowDropdown={setShowDropdown} />
      {showDropdown && (
        <div className="absolute right-0 top-12 z-10 flex flex-col gap-3 rounded-md bg-gray-800 p-4">
          <ProfileButton />
          <hr className="w-full border-neutral-600" />
          <LogoutButton />
        </div>
      )}
    </div>
  );
};
