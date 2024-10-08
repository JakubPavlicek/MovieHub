import { type FC, Fragment, type ReactNode } from "react";
import { MovieHeader } from "@/components/movie/MovieHeader";
import type { components } from "@/api/api";
import { Link } from "react-router-dom";

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
  return (
    <div>
      {items.map((item, index) => (
        <Fragment key={item.id}>
          <Link to={`/${type}/${item.name}`} className="text-neutral-300 hover:text-cyan-300">
            {item.name}
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
  const { name, description, countries, genres, releaseDate, posterUrl, director, productionCompanies, cast } =
    movieDetails;

  return (
    <div className="mt-10 grid justify-start gap-6 sm:grid-cols-[230px_1fr]">
      <img src={posterUrl} alt={name} className="hidden flex-none rounded-md border-2 border-gray-500 sm:block" />

      <div className="flex w-full flex-col gap-2 text-neutral-400">
        <MovieHeader movieDetails={movieDetails} />

        <p className="my-4">{description}</p>

        <InfoRow label="Country">
          <LinkList items={countries} type="country" />
        </InfoRow>
        <InfoRow label="Genres">
          <LinkList items={genres} type="genre" />
        </InfoRow>
        <InfoRow label="Released">
          <span>{releaseDate}</span>
        </InfoRow>
        <InfoRow label="Director">
          <Link to={`/director/${director.name}`} className="text-neutral-300 hover:text-cyan-300">
            {director.name}
          </Link>
        </InfoRow>
        <InfoRow label="Production">
          <LinkList items={productionCompanies} type="production" />
        </InfoRow>
        <InfoRow label="Cast">
          <LinkList items={cast} type="cast" />
        </InfoRow>
      </div>
    </div>
  );
};
