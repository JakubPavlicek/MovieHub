import type { FC } from "react";
import { MovieDetailRow } from "@/components/movie/MovieDetailRow";
import { formatList } from "@/utils/movieDetails";
import { MovieHeader } from "@/components/movie/MovieHeader";
import type { components } from "@/api/api";

interface MovieDetailsProps {
  movieDetails: components["schemas"]["MovieDetailsResponse"];
}

export const MovieDetails: FC<MovieDetailsProps> = ({ movieDetails }) => {
  const {
    name,
    description,
    countries,
    genres,
    releaseDate,
    posterUrl,
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
