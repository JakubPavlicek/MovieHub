import React from "react";
import { useAuth0 } from "@auth0/auth0-react";
import MobileMenuButton from "./components/MobileMenuButton.tsx";
import SearchBar from "./components/SearchBar.tsx";
import LoginButton from "./components/LoginButton.tsx";
import MobileSearchButton from "./components/MobileSearchButton.tsx";
import Logo from "./components/Logo.tsx";
import NavMenu from "./components/NavMenu.tsx";
import ProfileDropdown from "./components/ProfileDropdown.tsx";
import useMobileSearch from "../../hooks/useMobileSearch.tsx";
import MobileMenu from "./components/MobileMenu.tsx";
import useMobileMenu from "../../hooks/useMobileMenu.tsx";

// https://github.com/codyseibert/online-classroom/blob/cbcc7f6987e269be4ef125561aa0f532b8e055e2/src/components/common/Header/Header.tsx

const Header: React.FC = () => {
  const { isAuthenticated } = useAuth0();
  const { showMobileMenu, toggleMobileMenu } = useMobileMenu();
  const { showMobileSearch, toggleMobileSearch } = useMobileSearch();

  return (
    <header className="mt-6 text-white">
      <div className="mx-5 flex min-h-12 flex-row flex-nowrap items-center gap-2.5 lg:mx-10 lg:gap-10">
        <div className="flex lg:hidden">
          <MobileMenuButton showMobileMenu={showMobileMenu} toggleMobileMenu={toggleMobileMenu} />
        </div>
        <div className="mr-auto flex flex-shrink-0 items-center gap-10 text-gray-300">
          <Logo />
          <div className="hidden lg:block">
            <NavMenu />
          </div>
        </div>
        <div className="hidden w-full max-w-md sm:mr-auto sm:block">
          <SearchBar autoFocus={false} />
        </div>
        <div className="flex sm:hidden">
          <MobileSearchButton showMobileSearch={showMobileSearch} toggleMobileSearch={toggleMobileSearch} />
        </div>
        {isAuthenticated ? <ProfileDropdown /> : <LoginButton />}
      </div>
      {showMobileMenu && <MobileMenu toggleMobileMenu={toggleMobileMenu} />}
      {showMobileSearch && (
        <div className="mx-5 mt-4 sm:hidden">
          <SearchBar autoFocus={true} />
        </div>
      )}
    </header>
  );
};

export default Header;
