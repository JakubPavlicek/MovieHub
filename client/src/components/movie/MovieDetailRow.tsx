import type { FC } from "react";
import { Link } from "react-router-dom";

interface MovieDetailRowProps {
  label: string;
  values: string[] | undefined;
  destinationPrefix?: string;
}

export const MovieDetailRow: FC<MovieDetailRowProps> = ({ label, values, destinationPrefix }) => {
  return (
    <div className="grid grid-cols-[120px_1fr]">
      <span>{label}:</span>
      {values?.map((value, index) => (
        <Link to={`${destinationPrefix}/${value}`} key={value} className="hover:text-cyan-300">
          {value}
          {index !== values?.length ? "," : ""}
        </Link>
      ))}
    </div>
  );
};
