import type { FC } from "react";
import type { components } from "@/api/api";
import { useTranslation } from "react-i18next";

interface ReplyBodyProps {
  text: components["schemas"]["CommentInfoDetailsResponse"]["text"];
  isDeleted: components["schemas"]["CommentInfoDetailsResponse"]["isDeleted"];
}

export const ReplyBody: FC<ReplyBodyProps> = ({ text, isDeleted }) => {
  const { t } = useTranslation();
  const regex = /(@\w+\s)(.*)/;
  const match = regex.exec(text);

  const highlightedUser = match ? match[1] : "";
  const remainingText = match ? match[2] : text;

  const replyText = isDeleted ? t("components.comment.commentDeleted") : remainingText;

  return (
    <div>
      {highlightedUser && <span className="text-cyan-300">{highlightedUser}</span>}
      <span className={`${isDeleted ? "italic text-neutral-400" : "text-neutral-300"}`}>{replyText}</span>
    </div>
  );
};
