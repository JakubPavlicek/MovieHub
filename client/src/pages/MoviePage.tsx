import type { FC } from "react";
import { useParams } from "react-router-dom";
import ButtonSection from "@/components/movie/ButtonSection";
import placeholder from "@/assets/video/placeholder.mp4";
import MovieDetails from "@/components/movie/MovieDetails";

// https://flixwave.watch/home/

const MoviePage: FC = () => {
  const { movieId = "" } = useParams();

  return (
    <main className="mx-auto 2xl:container">
      <div className="mx-5 my-4 text-white">
        <video src={placeholder} controls className="my-6" />

        <ButtonSection />

        <hr className="mt-10 border-neutral-700" />

        <MovieDetails movieId={movieId} />

        <div className="mt-5">Comments</div>
      </div>
    </main>
  );
};

export default MoviePage;
