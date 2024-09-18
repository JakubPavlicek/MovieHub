import type { FC } from "react";
import useMovies from "@/hooks/useMovies";
import MoviePreviewList from "@/components/common/MoviePreviewList";

const HomePage: FC = () => {
  const { movies } = useMovies();

  return (
    <main className="mx-auto 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 text-3xl font-semibold">
          <span className="border-b-2 border-cyan-400">Online movies</span>
        </div>
        <MoviePreviewList movies={movies} />
      </div>
    </main>
  );
};

export default HomePage;
