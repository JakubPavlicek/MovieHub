import { type FC, useState } from "react";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useApi } from "@/context/ApiProvider";
import { Filter } from "lucide-react";
import { useGenres } from "@/hooks/useGenres";
import { CategorySelect } from "@/components/common/CategorySelect";
import { MultiValue } from "react-select";
import type { SelectOption } from "@/types/selectOption";
import { useCountries } from "@/hooks/useCountries";
import { YearSelect } from "@/components/common/YearSelect";

export const HomePage: FC = () => {
  const [showFilters, setShowFilters] = useState(true);

  const api = useApi();
  const { data: movies } = api.useQuery("get", "/movies", {
    params: {
      query: {
        limit: 20,
      },
    },
  });

  const { data: yearList } = api.useQuery("get", "/movies/years");

  const { genres } = useGenres();
  const { countries } = useCountries();

  const [selectedGenres, setSelectedGenres] = useState<MultiValue<SelectOption>>([]);
  const [selectedCountries, setSelectedCountries] = useState<MultiValue<SelectOption>>([]);
  const [selectedYears, setSelectedYears] = useState<MultiValue<SelectOption>>([]);

  if (!movies?.content || !yearList?.years) {
    return <div className="text-white">Empty</div>;
  }

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="flex flex-row items-center justify-between">
          <div className="mb-6 text-3xl font-semibold">
            <span className="border-b-2 border-cyan-400">Online movies</span>
          </div>
          <div>
            <button
              className="inline-flex items-center gap-2 rounded-md bg-gray-700 px-2 py-1 text-gray-200 hover:bg-gray-600"
              onClick={() => setShowFilters((prev) => !prev)}
            >
              <Filter size={18} />
              Filter
            </button>
          </div>
        </div>
        {showFilters && (
          <div className="mb-8 mt-2 text-white">
            <div className="flex flex-row flex-wrap items-center justify-center gap-4 sm:justify-start">
              <CategorySelect
                placeholder="Select genres"
                items={genres}
                selectedItems={selectedGenres}
                setSelectedItems={setSelectedGenres}
              />
              <CategorySelect
                placeholder="Select countries"
                items={countries}
                selectedItems={selectedCountries}
                setSelectedItems={setSelectedCountries}
              />
              <YearSelect
                placeholder="Select years"
                years={yearList.years}
                selectedYears={selectedYears}
                setSelectedYears={setSelectedYears}
              />
            </div>
          </div>
        )}
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
