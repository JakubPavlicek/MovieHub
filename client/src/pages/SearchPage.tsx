import { type FC, useMemo } from "react";
import type { MovieSearchParams } from "@/types/movie";
import useSearchMovies from "@/hooks/useSearchMovies";
import { useParams } from "react-router-dom";
import MoviePreviewList from "@/components/common/MoviePreviewList";

const SearchPage: FC = () => {
  const { query } = useParams();
  const movieSearchParams: MovieSearchParams = { keyword: query };
  const { movies } = useSearchMovies(movieSearchParams);

  const pageTitle = useMemo(() => `Search results for "${query}"`, [query]);

  return (
    <main className="mx-auto 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 text-3xl font-semibold">
          <span className="border-b-2 border-cyan-400">{pageTitle}</span>
        </div>
        <MoviePreviewList movies={movies} />
      </div>
    </main>
  );
};

export default SearchPage;
