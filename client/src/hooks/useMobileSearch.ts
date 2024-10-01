import { useCallback, useState } from "react";

export const useMobileSearch = () => {
  const [showMobileSearch, setShowMobileSearch] = useState<boolean>(false);

  const toggleMobileSearch = useCallback(() => {
    setShowMobileSearch((prev) => !prev);
  }, []);

  return { showMobileSearch, toggleMobileSearch };
};
