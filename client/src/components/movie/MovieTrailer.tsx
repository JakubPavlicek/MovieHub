import { type FC, useEffect, useRef } from "react";
import { X } from "lucide-react";
import { transformYouTubeUrl } from "@/utils/movieDetails";

interface MovieTrailerProps {
  showTrailer: boolean;
  setShowTrailer: (prevState: boolean) => void;
  trailerUrl: string;
}

export const MovieTrailer: FC<MovieTrailerProps> = ({ showTrailer, setShowTrailer, trailerUrl }) => {
  const dialogRef = useRef<HTMLDialogElement | null>(null);
  const trailerSrc = transformYouTubeUrl(trailerUrl);

  useEffect(() => {
    const dialog = dialogRef.current;

    if (!dialog) return;

    if (!showTrailer) {
      dialog.close();
      document.body.style.overflow = "";
      return;
    }

    dialog.showModal();
    dialog.focus();
    document.body.style.overflow = "hidden";
  }, [trailerUrl, showTrailer]);

  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (showTrailer && e.key === "Escape") {
        setShowTrailer(false);
      }
    };

    const handleClickOutside = (e: MouseEvent) => {
      if (showTrailer && (e.target as HTMLElement).id !== "trailer-button") {
        setShowTrailer(false);
      }
    };

    window.addEventListener("keydown", handleKeyDown);
    window.addEventListener("click", handleClickOutside);

    return () => {
      window.removeEventListener("keydown", handleKeyDown);
      window.removeEventListener("click", handleClickOutside);
    };
  }, [showTrailer, setShowTrailer]);

  return (
    <dialog
      ref={dialogRef}
      className="container fixed left-1/2 top-1/2 aspect-video w-full max-w-[95%] -translate-x-1/2 -translate-y-1/2 transform backdrop:bg-gray-950 backdrop:opacity-80 lg:max-w-screen-lg"
    >
      <iframe
        title={trailerUrl}
        src={showTrailer ? trailerSrc : ""}
        allowFullScreen
        className="aspect-video h-auto w-full bg-black"
      />
      <button
        className="absolute right-2 top-2 z-10 rounded-full p-1 text-white hover:bg-gray-800"
        onClick={() => setShowTrailer(false)}
      >
        <X />
      </button>
    </dialog>
  );
};
