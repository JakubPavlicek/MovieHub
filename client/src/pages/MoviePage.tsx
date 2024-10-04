import { type FC, useState } from "react";
import { useParams } from "react-router-dom";
import { ButtonSection } from "@/components/movie/ButtonSection";
import { MovieDetails } from "@/components/movie/MovieDetails";
import placeholder from "@/assets/video/placeholder.mp4";
import { useApi } from "@/context/ApiProvider";
import { MovieTrailer } from "@/components/movie/MovieTrailer";

export const MoviePage: FC = () => {
  const { movieId = "" } = useParams();
  const [showTrailer, setShowTrailer] = useState<boolean>(false);
  const api = useApi();

  const { data: movieDetails } = api.useQuery("get", "/movies/{movieId}", {
    params: {
      path: { movieId: movieId },
    },
  });

  return (
    <main className="mx-auto 2xl:container">
      <div className="mx-5 my-4 text-white">
        <video src={placeholder} controls className="my-6" />

        <ButtonSection showTrailer={showTrailer} setShowTrailer={setShowTrailer} />

        <hr className="mt-10 border-neutral-700" />

        <MovieDetails movieDetails={movieDetails} />

        <div className="mt-5">Comments</div>
      </div>
      <MovieTrailer
        showTrailer={showTrailer}
        setShowTrailer={setShowTrailer}
        trailerUrl={movieDetails?.trailerUrl}
      />
    </main>
  );
};
