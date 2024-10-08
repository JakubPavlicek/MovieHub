import type { components } from "@/api/api";
import type { FC } from "react";

interface CommentBodyProps {
  text: components["schemas"]["CommentDetailsResponse"]["text"];
  isDeleted: components["schemas"]["CommentDetailsResponse"]["isDeleted"];
}

export const CommentBody: FC<CommentBodyProps> = ({ text, isDeleted }) => {
  const regex = /(@\w+\s)(.*)/;
  const match = regex.exec(text);

  const highlightedUser = match ? match[1] : "";
  const remainingText = match ? match[2] : text;

  return (
    <div>
      {highlightedUser && <span className="text-cyan-300">{highlightedUser}</span>}
      <span className={`${isDeleted ? "italic text-neutral-400" : "text-neutral-300"}`}>{remainingText}</span>
    </div>
  );
};
