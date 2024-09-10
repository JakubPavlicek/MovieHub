import React from "react";
import MoviePreview from "../components/home/MoviePreview.tsx";
import useMovies from "../hooks/useMovies.tsx";

const Home: React.FC = () => {
  const { moviePage, isLoading, isError, error } = useMovies();

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (isError || !moviePage) {
    return <div>{JSON.stringify(error)}</div>;
  }

  console.log(moviePage);

  return (
    <main className="mx-5 mt-10 text-white lg:mx-10">
      <div className="flex flex-col justify-between">
        <div className="grid grid-cols-2 gap-x-3.5 gap-y-7 sm:grid-cols-[repeat(auto-fill,minmax(190px,1fr))]">
          {moviePage.content.map((movie) => (
            <MoviePreview key={movie.id} moviePreview={movie} />
          ))}
        </div>
      </div>
    </main>
  );
};

export default Home;
