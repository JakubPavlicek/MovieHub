import { useCallback, useState } from "react";

const useMobileSearch = () => {
  const [isMobileSearch, setIsMobileSearch] = useState<boolean>(false);

  const toggleMobileSearch = useCallback(() => {
    setIsMobileSearch((prevState) => !prevState);
  }, []);

  return { isMobileSearch, toggleMobileSearch };
};

export default useMobileSearch;
