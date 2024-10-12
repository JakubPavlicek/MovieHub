import { type FC, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { ChevronDown, ChevronUp } from "lucide-react";

export const LanguageDropdown: FC = () => {
  const [showDropdown, setShowDropdown] = useState(false);
  const { i18n } = useTranslation();
  const languages = Object.keys(i18n.options.resources ?? []);

  const [selectedLanguage, setSelectedLanguage] = useState<string>();

  useEffect(() => {
    setSelectedLanguage(i18n.language);
  }, [i18n.language]);

  const changeLanguage = (language: string) => {
    i18n.changeLanguage(language);
  };

  return (
    <div className="relative">
      <button
        className="group inline-flex items-center justify-between gap-1 rounded-lg border-2 border-gray-400 p-2 hover:border-gray-200"
        onClick={() => setShowDropdown(!showDropdown)}
      >
        <span className="hidden truncate md:block lg:max-w-36">{selectedLanguage}</span>
        {showDropdown ? <ChevronUp size={20} /> : <ChevronDown size={20} />}
      </button>
      {showDropdown && (
        <div className="absolute right-0 top-12 z-10 flex flex-col gap-2 rounded-md bg-gray-800 p-2">
          {languages.map((language) => (
            <button
              key={language}
              className="rounded-md px-4 py-1.5 hover:bg-gray-950 hover:text-cyan-300"
              onClick={() => changeLanguage(language)}
            >
              {language}
            </button>
          ))}
        </div>
      )}
    </div>
  );
};
