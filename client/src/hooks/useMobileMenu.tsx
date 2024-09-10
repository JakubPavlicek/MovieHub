import { useState } from "react";

const useMobileMenu = () => {
  const [showMobileMenu, setShowMobileMenu] = useState<boolean>(false);

  function toggleMobileMenu() {
    setShowMobileMenu((prev) => !prev);
  }

  return { showMobileMenu, toggleMobileMenu };
};

export default useMobileMenu;
