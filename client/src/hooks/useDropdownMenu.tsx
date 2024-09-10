import { useState } from "react";

const useDropdownMenu = () => {
  const [isOpen, setIsOpen] = useState(false);

  function toggleDropdown() {
    setIsOpen((prev) => !prev);
  }

  return { isOpen, toggleDropdown };
};

export default useDropdownMenu;
