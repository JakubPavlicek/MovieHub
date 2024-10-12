import { type FC, useEffect } from "react";
import { MessageSquareText, X } from "lucide-react";
import { useAuth0 } from "@auth0/auth0-react";
import { toast } from "react-toastify";
import { useTranslation } from "react-i18next";

interface ChatButtonProps {
  isChatOpened: boolean;
  setIsChatOpened: (prev: boolean) => void;
}

export const ChatButton: FC<ChatButtonProps> = ({ isChatOpened, setIsChatOpened }) => {
  const { t } = useTranslation();
  const { isAuthenticated } = useAuth0();

  useEffect(() => {
    if (!isAuthenticated && isChatOpened) {
      toast.error(t("toast.unauthenticated"));
    }
  }, [isAuthenticated, isChatOpened, t]);

  return (
    <div className="fixed bottom-8 right-8">
      <button
        className="rounded-xl bg-cyan-600 p-3 text-white hover:bg-cyan-500"
        onClick={() => setIsChatOpened(!isChatOpened)}
      >
        {isChatOpened ? <X size={28} strokeWidth={1.5} /> : <MessageSquareText size={28} strokeWidth={1.5} />}
      </button>
    </div>
  );
};
