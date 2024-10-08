import { FC } from "react";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useDirectorMovies } from "@/hooks/useDirectorMovies";

// TODO: make this work without the state
// TODO: add /directors/filter?name:James Wan - to fetch directors by name

export const DirectorPage: FC = () => {
  const { directorName, movies } = useDirectorMovies();

  if (!movies?.content) {
    return <div>Empty</div>;
  }

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 inline-flex max-w-fit gap-1.5 border-b-2 border-cyan-400 text-3xl font-semibold">
          <span className="capitalize">'{directorName}'</span>
          <span>movies</span>
        </div>
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
