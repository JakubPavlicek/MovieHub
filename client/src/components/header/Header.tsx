import React from "react";
import MobileMenuButton from "./components/MobileMenuButton.tsx";
import SearchBar from "./components/SearchBar.tsx";
import LoginButton from "./components/LoginButton.tsx";
import MobileSearchButton from "./components/MobileSearchButton.tsx";
import useMobileSearch from "../../hooks/useMobileSearch.tsx";
import Logo from "./components/Logo.tsx";
import NavMenu from "./components/NavMenu.tsx";
import { useAuth0 } from "@auth0/auth0-react";
import ProfileDropdown from "./components/ProfileDropdown.tsx";

// https://github.com/codyseibert/online-classroom/blob/cbcc7f6987e269be4ef125561aa0f532b8e055e2/src/components/common/Header/Header.tsx

const Header: React.FC = () => {
  const { user, isAuthenticated } = useAuth0();
  const { isMobileSearch, toggleMobileSearch } = useMobileSearch();

  console.log(user);

  return (
    <header className="mt-6 text-white">
      <div className="mx-5 flex min-h-12 flex-row flex-nowrap items-center gap-2.5 lg:mx-10 lg:gap-10">
        <MobileMenuButton />
        <div className="mr-auto flex-shrink-0 flex-row items-center gap-10 text-gray-300 md:flex">
          <Logo />
          <NavMenu />
        </div>
        <div className="hidden w-full max-w-md flex-row items-center sm:mr-auto sm:block">
          <SearchBar autoFocus={false} />
        </div>
        <MobileSearchButton isMobileSearch={isMobileSearch} toggleMobileSearch={toggleMobileSearch} />
        {isAuthenticated ? <ProfileDropdown /> : <LoginButton />}
      </div>
      {isMobileSearch && (
        <div className="mx-5 mt-4 sm:hidden">
          <SearchBar autoFocus={true} />
        </div>
      )}
    </header>
  );
};

export default Header;
