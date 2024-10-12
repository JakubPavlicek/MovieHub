import { type FC, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { ChevronDown, ChevronUp } from "lucide-react";
import { supportedLanguages } from "@/i18n/i18n";

export const LanguageDropdown: FC = () => {
  const [showDropdown, setShowDropdown] = useState(false);
  const { i18n } = useTranslation();

  const [selectedLanguage, setSelectedLanguage] = useState<string>(localStorage.getItem("language") ?? i18n.language);

  useEffect(() => {
    i18n.changeLanguage(localStorage.getItem("language") ?? i18n.language);
  }, [i18n]);

  const changeLanguage = (language: string) => {
    i18n.changeLanguage(language);
    localStorage.setItem("language", language);
    setSelectedLanguage(language);
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
          {supportedLanguages.map((language) => (
            <button
              key={language}
              className="rounded-md px-4 py-1.5 hover:bg-gray-950 hover:text-cyan-300"
              onClick={() => {
                changeLanguage(language);
                setShowDropdown(false);
              }}
            >
              {language}
            </button>
          ))}
        </div>
      )}
    </div>
  );
};
