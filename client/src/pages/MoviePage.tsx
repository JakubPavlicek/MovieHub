import type { FC } from "react";
import { useParams } from "react-router-dom";
import useMovieDetails from "@/hooks/useMovieDetails.ts";
import ButtonSection from "@/components/movie/ButtonSection.tsx";
import placeholder from "@/assets/video/placeholder.mp4";
import { Star } from "lucide-react";
import StarRating from "@/components/movie/StarRating.tsx";

// https://flixwave.watch/home/

const MoviePage: FC = () => {
  const { movieId = "" } = useParams();
  const { movieDetails } = useMovieDetails(movieId);

  if (!movieDetails) {
    return <div className="text-white">Empty</div>;
  }

  return (
    <main className="mx-auto 2xl:container">
      <div className="mx-5 my-4 text-white">
        <video src={placeholder} controls className="my-6" />
        <ButtonSection />
        <hr className="mt-10 border-neutral-700" />
        <div className="mt-10 flex flex-row gap-6">
          <img
            src={movieDetails.posterUrl}
            alt={movieDetails.name}
            width={200}
            className="hidden rounded-md border-2 border-gray-500 sm:block"
          />
          <div className="flex w-full flex-col gap-2 text-neutral-400">
            <div className="flex flex-row flex-wrap items-center justify-between gap-2">
              <div className="flex flex-col gap-2">
                <span className="text-2xl">{movieDetails.name}</span>
                <div className="flex flex-row gap-5">
                  <div className="flex flex-row items-center gap-1.5">
                    <Star size={20} className="fill-neutral-400" />
                    {movieDetails.rating}
                  </div>
                  <span>{movieDetails.duration} min</span>
                </div>
              </div>
              <div className="flex min-w-max flex-row gap-1">
                <StarRating />
                <span>({movieDetails.reviewCount} reviews)</span>
              </div>
            </div>
            <p className="my-4">{movieDetails.description}</p>
            <div className="flex flex-col gap-2">
              <div className="grid grid-cols-[120px_1fr]">
                <span className="">Country:</span>
                <span>{movieDetails.countries.map((country) => country.name).join(", ")}</span>
              </div>
              <div className="grid grid-cols-[120px_1fr]">
                <span>Genre:</span>
                <span>{movieDetails.genres.map((genre) => genre.name).join(", ")}</span>
              </div>
              <div className="grid grid-cols-[120px_1fr]">
                <span>Released:</span>
                <span>{movieDetails.releaseDate}</span>
              </div>
              <div className="grid grid-cols-[120px_1fr]">
                <span>Director:</span>
                <span>{movieDetails.director.name}</span>
              </div>
              <div className="grid grid-cols-[120px_1fr]">
                <span>Production:</span>
                <span>{movieDetails.productionCompanies.map((company) => company.name).join(", ")}</span>
              </div>
              <div className="grid grid-cols-[120px_1fr]">
                <span>Cast:</span>
                <span>{movieDetails.cast.map((actor) => actor.name).join(", ")}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
};

export default MoviePage;
