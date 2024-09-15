import type { FC } from "react";
import MoviePreview from "../components/home/MoviePreview.tsx";
import useMovies from "../hooks/useMovies.ts";

const Home: FC = () => {
  const { moviePage, isLoading, isError, error } = useMovies();

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (isError || !moviePage) {
    return <div>{JSON.stringify(error)}</div>;
  }

  console.log(moviePage);

  return (
    <main className="mx-auto 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="grid grid-cols-[repeat(auto-fill,minmax(160px,1fr))] gap-x-3.5 gap-y-10">
          {moviePage.content.map((movie) => (
            <MoviePreview key={movie.id} moviePreview={movie} />
          ))}
        </div>
      </div>
    </main>
  );
};

export default Home;
