import { type FC, useState } from "react";
import { useTranslation } from "react-i18next";
import type { components } from "@/api/api";
import { ReplyReactionSection } from "@/components/comment/reply/ReplyReactionSection";
import { ReplyInput } from "@/components/comment/reply/ReplyInput";

interface ReplyFooterProps {
  reply: components["schemas"]["CommentInfoDetailsResponse"];
  commentId: components["schemas"]["CommentInfoDetailsResponse"]["id"];
}

export const ReplyFooter: FC<ReplyFooterProps> = ({ reply, commentId }) => {
  const { t } = useTranslation();
  const [showInput, setShowInput] = useState(false);

  return (
    <>
      <div className="flex flex-row items-center justify-between">
        {!reply.isDeleted && (
          <>
            <button
              className="max-w-fit text-neutral-400 hover:text-cyan-300"
              onClick={() => setShowInput((prev) => !prev)}
            >
              {t("components.comment.reply")}
            </button>
            <ReplyReactionSection reply={reply} />
          </>
        )}
      </div>
      {showInput && (
        <ReplyInput commentId={commentId} replierUserName={reply.author.name} setShowInput={setShowInput} />
      )}
    </>
  );
};
