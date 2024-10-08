import type { FC } from "react";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useApi } from "@/context/ApiProvider";

export const HomePage: FC = () => {
  const api = useApi();
  const { data: movies } = api.useQuery("get", "/movies", {
    params: {
      query: {
        limit: 20,
      },
    },
  });

  if (!movies?.content) {
    return <div className="text-white">Empty</div>;
  }

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 text-3xl font-semibold">
          <span className="border-b-2 border-cyan-400">Online movies</span>
        </div>
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
