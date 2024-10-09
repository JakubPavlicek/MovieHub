import type { FC } from "react";

export const MovieSkeleton: FC = () => {
  return (
    <div className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mt-10 flex flex-col justify-between" />
    </div>
  );
};
