import { type FC, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useCountries } from "@/hooks/useCountries";
import { MoviePreviewList } from "@/components/common/MoviePreviewList";
import { useApi } from "@/context/ApiProvider";

export const CountryPage: FC = () => {
  const navigate = useNavigate();
  const { countryName = "" } = useParams();
  const { countryMap, getCountryId } = useCountries();
  const countryId = getCountryId(countryName) ?? "";
  const api = useApi();
  const { data: movies } = api.useQuery("get", "/countries/{countryId}/movies", {
    params: {
      path: { countryId: countryId },
    },
  });

  useEffect(() => {
    if (!countryMap.has(countryName)) {
      navigate("/", { replace: true });
    }
  }, [countryMap, countryName, navigate]);

  if (!movies?.content) {
    return <div>Empty</div>;
  }

  return (
    <main className="mx-auto 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 inline-flex max-w-fit gap-1.5 border-b-2 border-cyan-400 text-3xl font-semibold">
          <span className="capitalize">{countryName}</span>
          <span>movies</span>
        </div>
        <MoviePreviewList movies={movies.content} />
      </div>
    </main>
  );
};
