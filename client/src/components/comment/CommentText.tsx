import type { components } from "@/api/api";
import type { FC } from "react";
import { useTranslation } from "react-i18next";

interface CommentTextProps {
  text: components["schemas"]["CommentInfoDetailsResponse"]["text"];
  isDeleted: components["schemas"]["CommentInfoDetailsResponse"]["isDeleted"];
}

export const CommentText: FC<CommentTextProps> = ({ text, isDeleted }) => {
  const { t } = useTranslation();
  const regex = /(@\w+\s)(.*)/;
  const match = regex.exec(text);

  const highlightedUser = match ? match[1] : "";
  const remainingText = match ? match[2] : text;

  const displayText = isDeleted ? t("components.comment.commentDeleted") : remainingText;

  return (
    <div>
      {highlightedUser && <span className="text-cyan-300">{highlightedUser}</span>}
      <span className={`${isDeleted ? "italic text-neutral-400" : "text-neutral-300"}`}>{displayText}</span>
    </div>
  );
};
