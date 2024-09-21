import type { FC } from "react";
import { MessageSquare, Video } from "lucide-react";

const ButtonSection: FC = () => {
  return (
    <div className="flex items-center gap-3">
      <button className="group inline-flex gap-1 rounded-md border border-neutral-400 px-4 py-1.5 text-sm text-neutral-400 hover:border-neutral-300 hover:fill-neutral-300 hover:text-neutral-300">
        <Video strokeWidth={0} size={20} className="fill-neutral-400 group-hover:fill-neutral-300" />
        Trailer
      </button>
      <button className="group inline-flex gap-1 rounded-md border border-neutral-400 px-4 py-1.5 text-sm text-neutral-400 hover:border-neutral-300 hover:fill-neutral-300 hover:text-neutral-300">
        <MessageSquare
          strokeWidth={0}
          size={18}
          className="translate-y-0.5 fill-neutral-400 group-hover:fill-neutral-300"
        />
        Comment
      </button>
    </div>
  );
};

export default ButtonSection;
