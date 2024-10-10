import { type FC, useState } from "react";
import { useParams } from "react-router-dom";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useApi } from "@/context/ApiProvider";
import { MovieSkeleton } from "@/components/common/MovieSkeleton";
import { FilterButton } from "@/components/common/FilterButton";
import { FilterSelects } from "@/components/common/FilterSelects";

export const SearchPage: FC = () => {
  const [showFilters, setShowFilters] = useState(false);

  const { keyword = "" } = useParams();
  const api = useApi();
  const { data: movies } = api.useQuery("get", "/movies/search", {
    params: {
      query: {
        limit: 20,
        keyword: keyword,
      },
    },
  });

  if (!movies?.content) {
    return <MovieSkeleton />;
  }

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="flex flex-row items-center justify-between">
          <div className="mb-6 text-3xl font-semibold">
            <span className="border-b-2 border-cyan-400">{`Search results for "${keyword}"`}</span>
          </div>
          <FilterButton toggleFilters={() => setShowFilters((prev) => !prev)} />
        </div>
        <FilterSelects showFilters={showFilters} enableNavigate={true} />
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
