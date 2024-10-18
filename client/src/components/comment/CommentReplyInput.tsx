import React, { type FC, useState } from "react";
import type { components } from "@/api/api";
import { SendHorizontal } from "lucide-react";
import { useApi } from "@/context/ApiProvider";
import { useQueryClient } from "@tanstack/react-query";
import { useAuth0 } from "@auth0/auth0-react";
import { toast } from "react-toastify";
import { useTranslation } from "react-i18next";

interface CommentInputProps {
  movieId?: components["schemas"]["Uuid"];
  replierUserName: components["schemas"]["UserNameAndPictureUrl"]["name"];
  setShowInput: (prev: boolean) => void;
}

export const CommentReplyInput: FC<CommentInputProps> = ({ movieId = "", replierUserName, setShowInput }) => {
  const { t } = useTranslation();
  const [text, setText] = useState<string>("");
  const { isAuthenticated } = useAuth0();
  const queryClient = useQueryClient();
  const api = useApi();
  const { mutate } = api.useMutation("post", "/movies/{movieId}/comments", {
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["get", "/movies/{movieId}/comments"] }),
  });

  const submitComment = (text: string) => {
    if (!isAuthenticated) {
      toast.error(t("toast.unauthenticated"));
      return;
    }

    mutate({
      params: {
        path: { movieId: movieId },
      },
      body: {
        text: `@${replierUserName} ${text.trim()}`,
      },
    });
    setText("");
    setShowInput(false);
  };

  const handleEnterKey = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      submitComment(text);
    }
  };

  return (
    <div className="mt-2 flex min-h-11 gap-2">
      <input
        type="text"
        placeholder={t("components.comment.inputPlaceholder")}
        className="w-full rounded-full border-transparent bg-gray-700 px-4 text-white placeholder:text-gray-400 focus:border-cyan-300 focus:outline-none md:w-1/2 lg:w-1/3"
        value={text}
        onChange={(e) => setText(e.target.value)}
        onKeyDown={handleEnterKey}
        autoFocus
      />
      <button className="p-2 text-white hover:rounded-full hover:bg-gray-700" onClick={() => submitComment(text)}>
        <SendHorizontal strokeWidth={1} size={26} className="fill-cyan-600" />
      </button>
    </div>
  );
};
