import type { FC } from "react";

interface MovieDetailRowProps {
  label: string;
  value: string | undefined;
}

export const MovieDetailRow: FC<MovieDetailRowProps> = ({ label, value }) => {
  return (
    <div className="grid grid-cols-[120px_1fr]">
      <span>{label}:</span>
      <span>{value}</span>
    </div>
  );
};
