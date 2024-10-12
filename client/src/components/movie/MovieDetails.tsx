import { type FC, Fragment, type ReactNode } from "react";
import { MovieHeader } from "@/components/movie/MovieHeader";
import type { components } from "@/api/api";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";

interface InfoRowProps {
  label: string;
  children: ReactNode;
}

const InfoRow: FC<InfoRowProps> = ({ label, children }) => (
  <div className="grid grid-cols-[120px_1fr]">
    <span>{label}:</span>
    <div>{children}</div>
  </div>
);

interface LinkListItem {
  id: components["schemas"]["Uuid"];
  name: string;
}

interface LinkListProps {
  items: LinkListItem[];
  type: string;
}

const LinkList: FC<LinkListProps> = ({ items, type }) => {
  const { t } = useTranslation();

  const getTranslatedName = (name: string) => {
    switch (type) {
      case "genre":
        name = t(`genres.${name}.single`);
        break;
      case "country":
        name = t(`countries.${name}.single`);
        break;
      default:
        break;
    }

    return name;
  };

  return (
    <div>
      {items.map((item, index) => (
        <Fragment key={item.id}>
          <Link to={`/${type}/${item.name}`} className="text-neutral-300 hover:text-cyan-300">
            {getTranslatedName(item.name)}
          </Link>
          {index + 1 < (items.length || 0) && ", "}
        </Fragment>
      ))}
    </div>
  );
};

interface MovieDetailsProps {
  movieDetails: components["schemas"]["MovieDetailsResponse"];
}

export const MovieDetails: FC<MovieDetailsProps> = ({ movieDetails }) => {
  const { t } = useTranslation();
  const { name, description, countries, genres, releaseDate, posterUrl, director, productionCompanies, cast } =
    movieDetails;

  return (
    <div className="mt-10 grid justify-start gap-6 sm:grid-cols-[230px_1fr]">
      <img src={posterUrl} alt={name} className="hidden flex-none rounded-md border-2 border-gray-500 sm:block" />

      <div className="flex w-full flex-col gap-2 text-neutral-400">
        <MovieHeader movieDetails={movieDetails} />

        <p className="my-4">{description}</p>

        <InfoRow label={t("components.movie.countryLabel")}>
          <LinkList items={countries} type="country" />
        </InfoRow>
        <InfoRow label={t("components.movie.genreLabel")}>
          <LinkList items={genres} type="genre" />
        </InfoRow>
        <InfoRow label={t("components.movie.releasedLabel")}>
          <span>{releaseDate}</span>
        </InfoRow>
        <InfoRow label={t("components.movie.directorLabel")}>
          <Link to={`/director/${director.name}`} className="text-neutral-300 hover:text-cyan-300">
            {director.name}
          </Link>
        </InfoRow>
        <InfoRow label={t("components.movie.productionLabel")}>
          <LinkList items={productionCompanies} type="production" />
        </InfoRow>
        <InfoRow label={t("components.movie.castLabel")}>
          <LinkList items={cast} type="cast" />
        </InfoRow>
      </div>
    </div>
  );
};
