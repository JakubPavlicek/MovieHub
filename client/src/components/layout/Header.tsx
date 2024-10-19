import { type FC, useState } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import { MobileMenuButton } from "@/components/header/mobile/MobileMenuButton";
import { SearchBar } from "@/components/header/desktop/SearchBar";
import { LoginButton } from "@/components/header/desktop/LoginButton";
import { MobileSearchButton } from "@/components/header/mobile/MobileSearchButton";
import { Logo } from "@/components/header/desktop/Logo";
import { NavMenu } from "@/components/header/desktop/NavMenu";
import { ProfileDropdown } from "@/components/header/profile/ProfileDropdown";
import { MobileMenu } from "@/components/header/mobile/MobileMenu";
import { useMobileSearch } from "@/hooks/useMobileSearch";
import { LanguageDropdown } from "@/components/header/desktop/LanguageDropdown";

export const Header: FC = () => {
  const { isAuthenticated } = useAuth0();
  const [showMobileMenu, setShowMobileMenu] = useState(false);
  const { showMobileSearch, toggleMobileSearch } = useMobileSearch();

  return (
    <header className="mx-auto mt-6 text-white 2xl:container">
      <nav className="mx-5 flex min-h-12 flex-nowrap items-center gap-2.5">
        <div className="flex lg:hidden">
          <MobileMenuButton
            showMobileMenu={showMobileMenu}
            toggleMobileMenu={() => setShowMobileMenu((prev) => !prev)}
          />
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
        <div className="hidden lg:block">
          <LanguageDropdown />
        </div>
        {isAuthenticated ? <ProfileDropdown /> : <LoginButton />}
      </nav>
      {showMobileMenu && (
        <div className="lg:hidden">
          <MobileMenu toggleMobileMenu={() => setShowMobileMenu((prev) => !prev)} />
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
