import { type FC } from "react";
import { useParams } from "react-router-dom";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useApi } from "@/context/ApiProvider";

export const SearchPage: FC = () => {
  const { keyword = "" } = useParams();
  const api = useApi();
  const { data: movies } = api.useQuery("get", "/movies/search", {
    params: {
      query: {
        page: 0,
        limit: 50,
        sort: "name.asc",
        keyword: keyword,
      },
    },
  });

  if (!movies?.content) {
    return <div className="text-white">Empty</div>;
  }

  return (
    <main className="mx-auto 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 text-3xl font-semibold">
          <span className="border-b-2 border-cyan-400">{`Search results for "${keyword}"`}</span>
        </div>
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
