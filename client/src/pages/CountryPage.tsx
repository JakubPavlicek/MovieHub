import { type FC, useEffect, useMemo } from "react";
import { useNavigate, useParams } from "react-router-dom";
import useCountries from "@/hooks/useCountries";
import useCountryMovies from "@/hooks/useCountryMovies";
import MoviePreviewList from "@/components/common/MoviePreviewList";

const CountryPage: FC = () => {
  const navigate = useNavigate();
  const { countryName = "" } = useParams();
  const { countryMap, getCountryId, isLoadingCountries } = useCountries();
  const { movies } = useCountryMovies(getCountryId(countryName));

  useEffect(() => {
    if (!isLoadingCountries && !countryMap.has(countryName)) {
      navigate("/", { replace: true });
    }
  }, [isLoadingCountries, countryMap, countryName, navigate]);

  const pageTitle = useMemo(
    () => `${countryName.charAt(0).toUpperCase() + countryName.slice(1)} movies`,
    [countryName],
  );

  return (
    <main className="mx-auto 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 text-3xl font-semibold">
          <span className="border-b-2 border-cyan-400">{pageTitle}</span>
        </div>
        <MoviePreviewList movies={movies} />
      </div>
    </main>
  );
};

export default CountryPage;
