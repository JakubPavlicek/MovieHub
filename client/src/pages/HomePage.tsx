import type { FC } from "react";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useApi } from "@/context/ApiProvider";

export const HomePage: FC = () => {
  // const { movies } = useMovies();
  const api = useApi();
  const { data: movies } = api.useQuery("get", "/movies", { params: { query: { limit: 20 } } });

  if (!movies) return "Loading...";

  return (
    <main className="mx-auto 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 text-3xl font-semibold">
          <span className="border-b-2 border-cyan-400">Online movies</span>
        </div>
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
