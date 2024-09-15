import { useCallback, useState } from "react";

const useDropdownMenu = () => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleDropdown = useCallback(() => {
    setIsOpen((prev) => !prev);
  }, []);

  return { isOpen, toggleDropdown };
};

export default useDropdownMenu;
