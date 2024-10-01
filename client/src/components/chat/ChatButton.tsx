import type { FC } from "react";
import { MessageSquareText, X } from "lucide-react";

interface ChatButtonProps {
  isChatOpened: boolean;
  setIsChatOpened: (prev: boolean) => void;
}

export const ChatButton: FC<ChatButtonProps> = ({ isChatOpened, setIsChatOpened }) => {
  return (
    <div className="absolute bottom-8 right-6">
      <button
        className="rounded-xl bg-cyan-600 p-3 text-white hover:bg-cyan-500"
        onClick={() => setIsChatOpened(!isChatOpened)}
      >
        {isChatOpened ? <X size={28} strokeWidth={1.5} /> : <MessageSquareText size={28} strokeWidth={1.5} />}
      </button>
    </div>
  );
};
