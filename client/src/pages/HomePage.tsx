import { type FC } from "react";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useApi } from "@/context/ApiProvider";
import { MovieSkeleton } from "@/components/common/MovieSkeleton";
import { FilterButton } from "@/components/common/FilterButton";
import { FilterSelects } from "@/components/common/FilterSelects";
import { useFilterSelects } from "@/hooks/useFilterSelects";

export const HomePage: FC = () => {
  const {
    selectedGenres,
    setSelectedGenres,
    selectedCountries,
    setSelectedCountries,
    selectedYears,
    setSelectedYears,
    showFilters,
    setShowFilters,
  } = useFilterSelects(false, true);

  const api = useApi();
  const { data: movies } = api.useQuery("get", "/movies", {
    params: {
      query: { limit: 20 },
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
        <FilterSelects
          selectedGenres={selectedGenres}
          setSelectedGenres={setSelectedGenres}
          selectedCountries={selectedCountries}
          setSelectedCountries={setSelectedCountries}
          selectedYears={selectedYears}
          setSelectedYears={setSelectedYears}
          showFilters={showFilters}
        />
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
