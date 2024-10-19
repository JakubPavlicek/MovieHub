import type { components } from "@/api/api";
import { type FC, useState } from "react";
import { CommentReactionSection } from "@/components/comment/CommentReactionSection";
import { CommentInput } from "@/components/comment/CommentInput";
import { useTranslation } from "react-i18next";

interface CommentFooterProps {
  item: components["schemas"]["CommentInfoDetailsResponse"];
  commentId: components["schemas"]["CommentInfoDetailsResponse"]["id"];
  isReply?: boolean;
}

export const CommentFooter: FC<CommentFooterProps> = ({ item, commentId, isReply = false }) => {
  const { t } = useTranslation();
  const [showInput, setShowInput] = useState(false);

  return (
    <>
      <div className="flex flex-row items-center justify-between">
        {!item.isDeleted && (
          <>
            <button
              className="max-w-fit text-neutral-400 hover:text-cyan-300"
              onClick={() => setShowInput((prev) => !prev)}
            >
              {t("components.comment.reply")}
            </button>
            <CommentReactionSection item={item} isReply={isReply} />
          </>
        )}
      </div>
      {showInput && (
        <CommentInput commentId={commentId} replierUserName={item.author.name} setShowInput={setShowInput} />
      )}
    </>
  );
};
