import type { components } from "@/api/api";
import { type FC, useState } from "react";
import { CommentReactionSection } from "@/components/comment/CommentReactionSection";
import { CommentReplyInput } from "@/components/comment/CommentReplyInput";

interface CommentFooterProps {
  comment: components["schemas"]["CommentDetailsResponse"];
  topLevelCommentId: components["schemas"]["ParentCommentUuid"];
}

export const CommentFooter: FC<CommentFooterProps> = ({ comment, topLevelCommentId }) => {
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
              Reply
            </button>
            <CommentReactionSection comment={comment} />
          </>
        )}
      </div>
      {showInput && (
        <CommentReplyInput
          movieId={comment.movieId}
          parentCommentId={topLevelCommentId}
          replierUserName={comment.author.name}
          setShowInput={setShowInput}
        />
      )}
    </>
  );
};
