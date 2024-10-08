import { FC } from "react";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useProductionMovies } from "@/hooks/useProductionMovies";

export const ProductionPage: FC = () => {
  const { companyName, movies } = useProductionMovies();

  if (!movies?.content) {
    return <div>Empty</div>;
  }

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 inline-flex max-w-fit gap-1.5 border-b-2 border-cyan-400 text-3xl font-semibold">
          <span className="capitalize">'{companyName}'</span>
          <span>movies</span>
        </div>
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
