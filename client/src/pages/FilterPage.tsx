import { FC, useState } from "react";
import { useApi } from "@/context/ApiProvider";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { MovieSkeleton } from "@/components/common/MovieSkeleton";
import { FilterButton } from "@/components/common/FilterButton";
import { FilterSelects } from "@/components/common/FilterSelects";
import { filterAll, filterParams } from "@/types/filterParams";
import { useSearchParams } from "react-router-dom";

export const FilterPage: FC = () => {
  const [searchParams] = useSearchParams();
  const [showFilters, setShowFilters] = useState(true);

  const api = useApi();
  const { data: movies } = api.useQuery("get", "/movies/filter", {
    params: {
      query: {
        limit: 20,
        releaseYear: searchParams.get(filterParams.years) ?? filterAll,
        genre: searchParams.get(filterParams.genres) ?? filterAll,
        country: searchParams.get(filterParams.countries) ?? filterAll,
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
            <span className="border-b-2 border-cyan-400">Online movies</span>
          </div>
          <FilterButton toggleFilters={() => setShowFilters((prev) => !prev)} />
        </div>
        <FilterSelects showFilters={showFilters} enableNavigate={false} />
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
