import type { FC } from "react";
import { Link } from "react-router-dom";
import type { components } from "@/api/api";
import { useTranslation } from "react-i18next";

interface MobileDropdownItemsProps {
  isOpen: boolean;
  type: "genre" | "country";
  items: components["schemas"]["GenreDetailsResponse"][] | components["schemas"]["CountryDetailsResponse"][];
}

export const MobileDropdownItems: FC<MobileDropdownItemsProps> = ({ isOpen, type, items }) => {
  const { t } = useTranslation();

  return (
    <div className={`grid w-full transition-all duration-300 ${isOpen ? "grid-rows-[1fr]" : "grid-rows-[0fr]"}`}>
      <div className="grid grid-cols-2 overflow-hidden">
        {items.map((item) => (
          <Link
            to={`/${type}/${item.name}`}
            key={item.id}
            className="min-h-10 truncate py-2.5 text-left text-sm text-gray-400 hover:text-white"
          >
            {type === "genre" ? t(`genres.${item.name}.single`) : t(`countries.${item.name}.single`)}
          </Link>
        ))}
      </div>
    </div>
  );
};
