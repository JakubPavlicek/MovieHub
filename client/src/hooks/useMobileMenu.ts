import { useCallback, useState } from "react";

export const useMobileMenu = () => {
  const [showMobileMenu, setShowMobileMenu] = useState(false);

  const toggleMobileMenu = useCallback(() => {
    setShowMobileMenu((prev) => !prev);
  }, []);

  return { showMobileMenu, toggleMobileMenu };
};
