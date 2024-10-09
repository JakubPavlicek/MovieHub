import { FC } from "react";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useActorMovies } from "@/hooks/useActorMovies";
import { MovieSkeleton } from "@/components/common/MovieSkeleton";

export const CastPage: FC = () => {
  const { actorName, movies } = useActorMovies();

  if (!movies?.content) {
    return <MovieSkeleton />;
  }

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 inline-flex max-w-fit gap-1.5 border-b-2 border-cyan-400 text-3xl font-semibold">
          <span className="capitalize">'{actorName}'</span>
          <span>movies</span>
        </div>
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
