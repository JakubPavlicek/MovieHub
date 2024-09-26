import type { FC } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import MobileMenuButton from "@/components/header/MobileMenuButton";
import SearchBar from "@/components/header/SearchBar";
import LoginButton from "@/components/header/LoginButton";
import MobileSearchButton from "@/components/header/MobileSearchButton";
import Logo from "@/components/header/Logo";
import NavMenu from "@/components/header/NavMenu";
import ProfileDropdown from "@/components/header/ProfileDropdown";
import MobileMenu from "@/components/header/MobileMenu";
import useMobileMenu from "@/hooks/useMobileMenu";
import useMobileSearch from "@/hooks/useMobileSearch";

// https://github.com/codyseibert/online-classroom/blob/cbcc7f6987e269be4ef125561aa0f532b8e055e2/src/components/common/Header/Header.tsx
// NodeJS: https://github.com/nodejs/nodejs.org/tree/4139bbbd21d3a6bdbde5c89fb94ebf1c17d6ccc2/apps/site

const Header: FC = () => {
  const { isAuthenticated } = useAuth0();
  const { showMobileMenu, toggleMobileMenu } = useMobileMenu();
  const { showMobileSearch, toggleMobileSearch } = useMobileSearch();

  return (
    <header className="mx-auto mt-6 text-white 2xl:container">
      <nav className="mx-5 flex min-h-12 flex-nowrap items-center gap-2.5">
        <div className="flex lg:hidden">
          <MobileMenuButton showMobileMenu={showMobileMenu} toggleMobileMenu={toggleMobileMenu} />
        </div>
        <div className="mr-auto flex flex-shrink-0 items-center gap-4 text-gray-300 xl:gap-8">
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
      </nav>
      {showMobileMenu && (
        <div className="lg:hidden">
          <MobileMenu toggleMobileMenu={toggleMobileMenu} />
        </div>
      )}
      {showMobileSearch && (
        <div className="mx-5 mt-4 sm:hidden">
          <SearchBar autoFocus={true} />
        </div>
      )}
    </header>
  );
};

export default Header;
