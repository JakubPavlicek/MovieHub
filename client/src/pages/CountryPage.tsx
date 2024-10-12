import { type FC, useState } from "react";
import { useParams } from "react-router-dom";
import { useCountries } from "@/hooks/useCountries";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useApi } from "@/context/ApiProvider";
import { MovieSkeleton } from "@/components/common/MovieSkeleton";
import { FilterButton } from "@/components/common/FilterButton";
import { FilterSelects } from "@/components/common/FilterSelects";
import { useTranslation } from "react-i18next";

export const CountryPage: FC = () => {
  const { t } = useTranslation();
  const [showFilters, setShowFilters] = useState(false);

  const { countryName = "" } = useParams();
  const { getCountryId } = useCountries();
  const api = useApi();
  const { data: movies } = api.useQuery("get", "/countries/{countryId}/movies", {
    params: {
      path: { countryId: getCountryId(countryName) ?? "" },
    },
  });

  if (!movies?.content) {
    return <MovieSkeleton />;
  }

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="flex flex-row items-center justify-between">
          <div className="mb-6 inline-flex max-w-fit gap-1.5 border-b-2 border-cyan-400 text-3xl font-semibold">
            <span className="capitalize">{t(`countries.${countryName}.plural`)}</span>
            <span>{t("components.page.movieTitleSuffix")}</span>
          </div>
          <FilterButton toggleFilters={() => setShowFilters((prev) => !prev)} />
        </div>
        <FilterSelects showFilters={showFilters} enableNavigate={true} />
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
