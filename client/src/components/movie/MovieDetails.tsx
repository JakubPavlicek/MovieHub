import type { FC } from "react";
import MovieDetailRow from "@/components/movie/MovieDetailRow";
import { formatList } from "@/utils/movieDetails";
import useMovieDetails from "@/hooks/useMovieDetails";
import MovieHeader from "@/components/movie/MovieHeader";

interface MovieDetailsProps {
  movieId: string;
}

const MovieDetails: FC<MovieDetailsProps> = ({ movieId }) => {
  const { movieDetails } = useMovieDetails(movieId);

  if (!movieDetails) {
    return <div className="text-white">Empty</div>;
  }

  const {
    posterUrl,
    name,
    description,
    countries,
    genres,
    releaseDate,
    director,
    productionCompanies,
    cast,
  } = movieDetails;

  return (
    <div className="mt-10 grid justify-start gap-6 sm:grid-cols-[230px_1fr]">
      <img
        src={posterUrl}
        alt={name}
        className="hidden flex-none rounded-md border-2 border-gray-500 sm:block"
      />

      <div className="flex w-full flex-col gap-2 text-neutral-400">
        <MovieHeader movieDetails={movieDetails} />

        <p className="my-4">{description}</p>

        <MovieDetailRow label="Country" value={formatList(countries)} />
        <MovieDetailRow label="Genre" value={formatList(genres)} />
        <MovieDetailRow label="Released" value={releaseDate} />
        <MovieDetailRow label="Director" value={director?.name} />
        <MovieDetailRow label="Production" value={formatList(productionCompanies)} />
        <MovieDetailRow label="Cast" value={formatList(cast)} />
      </div>
    </div>
  );
};

export default MovieDetails;
