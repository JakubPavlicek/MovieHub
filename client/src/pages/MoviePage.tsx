import { type FC, useState } from "react";
import { useParams } from "react-router-dom";
import { useApi } from "@/context/ApiProvider";
import { TrailerButton } from "@/components/movie/TrailerButton";
import { MovieDetails } from "@/components/movie/MovieDetails";
import { MovieTrailer } from "@/components/movie/MovieTrailer";
import { Comments } from "@/components/comment/Comments";
import placeholder from "@/assets/video/placeholder.mp4";
import { MovieSkeleton } from "@/components/common/MovieSkeleton";
import { MovieCommentInput } from "@/components/movie/MovieCommentInput";

export const MoviePage: FC = () => {
  const { movieId = "" } = useParams();
  const [showTrailer, setShowTrailer] = useState(false);

  const api = useApi();
  const { data: movieDetails } = api.useQuery("get", "/movies/{movieId}", {
    params: {
      path: { movieId: movieId },
    },
  });

  if (!movieDetails) {
    return <MovieSkeleton />;
  }

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mx-5 my-4 text-white">
        <video src={placeholder} controls className="my-6" />

        <TrailerButton showTrailer={showTrailer} setShowTrailer={setShowTrailer} />

        <hr className="mt-10 border-neutral-700" />

        <MovieDetails movieDetails={movieDetails} />

        <Comments movieId={movieId} />

        <MovieCommentInput movieId={movieId} />
      </div>
      <MovieTrailer showTrailer={showTrailer} setShowTrailer={setShowTrailer} trailerUrl={movieDetails.trailerUrl} />
    </main>
  );
};
