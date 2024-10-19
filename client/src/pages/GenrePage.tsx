import { FC, useState } from "react";
import { useParams } from "react-router-dom";
import { useGenres } from "@/hooks/useGenres";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useApi } from "@/context/ApiProvider";
import { MovieSkeleton } from "@/components/common/MovieSkeleton";
import { FilterButton } from "@/components/common/FilterButton";
import { FilterSelects } from "@/components/common/FilterSelects";
import { useTranslation } from "react-i18next";

export const GenrePage: FC = () => {
  const { t } = useTranslation();
  const [showFilters, setShowFilters] = useState(false);

  const { genreName = "" } = useParams();
  const { getGenreId } = useGenres();
  const api = useApi();
  const { data: movies } = api.useQuery("get", "/genres/{genreId}/movies", {
    params: {
      path: { genreId: getGenreId(genreName) ?? "" },
    },
  });

  if (!movies?.content) {
    return <MovieSkeleton />;
  }

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="flex flex-row flex-wrap items-center justify-between">
          <div className="mb-6 inline-flex max-w-fit gap-1.5 border-b-2 border-cyan-400 text-3xl font-semibold">
            {t(`genres.${genreName}.plural`)} {t("components.page.movieTitleSuffix")}
          </div>
          <FilterButton toggleFilters={() => setShowFilters((prev) => !prev)} />
        </div>
        <FilterSelects showFilters={showFilters} enableNavigate={true} />
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
