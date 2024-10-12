import { type FC } from "react";
import { Video } from "lucide-react";
import { useTranslation } from "react-i18next";

interface ButtonSectionProps {
  showTrailer: boolean;
  setShowTrailer: (state: boolean) => void;
}

export const TrailerButton: FC<ButtonSectionProps> = ({ setShowTrailer, showTrailer }) => {
  const { t } = useTranslation();
  return (
    <button
      className="group inline-flex gap-1 rounded-md border border-neutral-400 px-4 py-1.5 text-sm text-neutral-400 hover:border-neutral-300 hover:fill-neutral-300 hover:text-neutral-300"
      onClick={() => setShowTrailer(!showTrailer)}
      id="trailer-button"
    >
      <Video strokeWidth={0} size={20} className="fill-neutral-400 group-hover:fill-neutral-300" />
      {t("components.movie.trailer")}
    </button>
  );
};
