import { useCallback, useState } from "react";

const useMobileMenu = () => {
  const [showMobileMenu, setShowMobileMenu] = useState<boolean>(false);

  const toggleMobileMenu = useCallback(() => {
    setShowMobileMenu((prev) => !prev);
  }, []);

  return { showMobileMenu, toggleMobileMenu };
};

export default useMobileMenu;
