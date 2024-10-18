import type { components } from "@/api/api";
import { type FC, useState } from "react";
import { CommentReactionSection } from "@/components/comment/CommentReactionSection";
import { CommentReplyInput } from "@/components/comment/CommentReplyInput";
import { useTranslation } from "react-i18next";

interface CommentFooterProps {
  comment: components["schemas"]["CommentInfoDetailsResponse"];
  movieId: components["schemas"]["MovieDetailsResponse"]["id"];
}

export const CommentFooter: FC<CommentFooterProps> = ({ comment, movieId }) => {
  const { t } = useTranslation();
  const [showInput, setShowInput] = useState(false);

  return (
    <>
      <div className="flex flex-row items-center justify-between">
        {!comment.isDeleted && (
          <>
            <button
              className="max-w-fit text-neutral-400 hover:text-cyan-300"
              onClick={() => setShowInput((prev) => !prev)}
            >
              {t("components.comment.reply")}
            </button>
            <CommentReactionSection comment={comment} />
          </>
        )}
      </div>
      {showInput && (
        <CommentReplyInput movieId={movieId} replierUserName={comment.author.name} setShowInput={setShowInput} />
      )}
    </>
  );
};
