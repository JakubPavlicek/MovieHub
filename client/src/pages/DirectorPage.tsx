import { FC, useState } from "react";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useDirectorMovies } from "@/hooks/useDirectorMovies";
import { MovieSkeleton } from "@/components/common/MovieSkeleton";
import { FilterButton } from "@/components/common/FilterButton";
import { FilterSelects } from "@/components/common/FilterSelects";
import { useTranslation } from "react-i18next";

export const DirectorPage: FC = () => {
  const { t } = useTranslation();
  const [showFilters, setShowFilters] = useState(false);

  const { directorName, movies } = useDirectorMovies();

  if (!movies?.content) {
    return <MovieSkeleton />;
  }

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="flex flex-row flex-wrap items-center justify-between">
          <div className="mb-6 inline-flex max-w-fit gap-1.5 border-b-2 border-cyan-400 text-3xl font-semibold">
            {`'${directorName}'`} {t("components.page.movieTitleSuffix")}
          </div>
          <FilterButton toggleFilters={() => setShowFilters((prev) => !prev)} />
        </div>
        <FilterSelects showFilters={showFilters} enableNavigate={true} />
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
