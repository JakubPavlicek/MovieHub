import { FC, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useGenres } from "@/hooks/useGenres";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useApi } from "@/context/ApiProvider";

export const GenrePage: FC = () => {
  const navigate = useNavigate();
  const { genreName = "" } = useParams();
  const { genreMap, getGenreId } = useGenres();
  const genreId = getGenreId(genreName) ?? "";
  const api = useApi();
  const { data: movies } = api.useQuery("get", "/genres/{genreId}/movies", {
    params: {
      path: { genreId: genreId },
    },
  });

  useEffect(() => {
    if (!genreMap.has(genreName)) {
      navigate("/", { replace: true });
    }
  }, [genreMap, genreName, navigate]);

  if (!movies?.content) {
    return <div>Empty</div>;
  }

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 inline-flex max-w-fit gap-1.5 border-b-2 border-cyan-400 text-3xl font-semibold">
          <span className="capitalize">{genreName}</span>
          <span>movies</span>
        </div>
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
