import type { FC } from "react";
import { useTranslation } from "react-i18next";
import { useDropdownMenu } from "@/hooks/useDropdownMenu";
import { ChevronDown, ChevronUp } from "lucide-react";
import { supportedLanguages } from "@/i18n/i18n";

interface LanguageProps {
  language: string;
  changeLanguage: (language: string) => void;
  isOpen: boolean;
}

const Language: FC<LanguageProps> = ({ language, changeLanguage, isOpen }) => {
  return (
    <div className={`grid w-full transition-all duration-300 ${isOpen ? "grid-rows-[1fr]" : "grid-rows-[0fr]"}`}>
      <div className="grid grid-cols-1 overflow-hidden">
        <button
          className="min-h-10 truncate py-2.5 text-left text-sm text-gray-400 hover:text-white"
          onClick={() => changeLanguage(language)}
        >
          {language}
        </button>
      </div>
    </div>
  );
};

export const MobileLanguageDropdown: FC = () => {
  const { t } = useTranslation();
  const { isOpen, toggleDropdown } = useDropdownMenu();
  const { i18n } = useTranslation();

  return (
    <div className="w-full">
      <button className="inline-flex w-full justify-between py-3 hover:text-cyan-300" onClick={toggleDropdown}>
        {t("components.header.mobile.language")}
        {isOpen ? <ChevronUp size={24} /> : <ChevronDown size={24} />}
      </button>
      {supportedLanguages.map((language) => (
        <Language
          key={language}
          language={language}
          changeLanguage={() => i18n.changeLanguage(language)}
          isOpen={isOpen}
        />
      ))}
    </div>
  );
};
