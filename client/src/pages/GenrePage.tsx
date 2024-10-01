import { FC, useEffect, useMemo } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useGenreMovies } from "@/hooks/useGenreMovies";
import { useGenres } from "@/hooks/useGenres";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";

export const GenrePage: FC = () => {
  const navigate = useNavigate();
  const { genreName = "" } = useParams();
  const { genreMap, getGenreId, isLoadingGenres } = useGenres();
  const { movies } = useGenreMovies(getGenreId(genreName));

  useEffect(() => {
    if (!isLoadingGenres && !genreMap.has(genreName)) {
      navigate("/", { replace: true });
    }
  }, [isLoadingGenres, genreMap, genreName, navigate]);

  const pageTitle = useMemo(
    () => `${genreName.charAt(0).toUpperCase() + genreName.slice(1)} movies`,
    [genreName],
  );

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
