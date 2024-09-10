import { useCallback, useState } from "react";

const useMobileSearch = () => {
  const [showMobileSearch, setShowMobileSearch] = useState<boolean>(false);

  const toggleMobileSearch = useCallback(() => {
    setShowMobileSearch((prevState) => !prevState);
  }, []);

  return { showMobileSearch, toggleMobileSearch };
};

export default useMobileSearch;
