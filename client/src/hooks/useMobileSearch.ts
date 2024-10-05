import { useCallback, useState } from "react";

export const useMobileSearch = () => {
  const [showMobileSearch, setShowMobileSearch] = useState(false);

  const toggleMobileSearch = useCallback(() => {
    setShowMobileSearch((prev) => !prev);
  }, []);

  return { showMobileSearch, toggleMobileSearch };
};
