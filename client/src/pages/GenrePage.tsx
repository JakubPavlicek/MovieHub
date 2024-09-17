import type { FC } from "react";
import useSearchGenreMovies from "@/hooks/useSearchGenreMovies.ts";
import { useParams } from "react-router-dom";
import useGenres from "@/hooks/useGenres.ts";
import MoviePreviewCard from "@/components/home/MoviePreviewCard.tsx";

const GenrePage: FC = () => {
  const { genreName } = useParams();
  const { genreMap } = useGenres();
  const genreId = genreMap.get(genreName!);
  const { movies } = useSearchGenreMovies(genreId!);

  // TODO: make less api calls ? + use GET /genres/{genreId} to fetch the Details of the genre (usefull for capitalized genre name)
  // TODO: create "Movie List" component

  return (
    <main className="mx-auto 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 text-3xl font-semibold">
          <span className="border-b-2 border-cyan-400">{genreName![0].toUpperCase() + genreName!.slice(1)} movies</span>
        </div>
        <div className="grid grid-cols-[repeat(auto-fill,minmax(160px,1fr))] gap-x-3.5 gap-y-10">
          {movies.map((movie) => (
            <MoviePreviewCard key={movie.id} moviePreview={movie} />
          ))}
        </div>
      </div>
    </main>
  );
};

export default GenrePage;
