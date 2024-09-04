import React from "react";
import MoviePreview from "../components/home/MoviePreview.tsx";
import { useQuery } from "@tanstack/react-query";

export interface IMoviePreview {
  movieId: number;
  name: string;
  releaseYear: number;
  duration: string;
  posterUrl: string;
  genres: string[];
}

interface Pageable {
  pageNumber: number;
  pageSize: number;
  sort: Sort;
  offset: number;
  paged: boolean;
  unpaged: boolean;
}

interface Sort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

interface MoviePage {
  content: IMoviePreview[];
  pageable: Pageable;
  last: boolean;
  totalElements: number;
  totalPages: number;
  first: boolean;
  size: number;
  number: number;
  sort: Sort;
  numberOfElements: number;
}

async function fetchMovies() {
  const data = await fetch("http://localhost:8088/movies");
  const movies = (await data.json()) as MoviePage;

  return movies;
}

const Home: React.FC = () => {
  const {
    data: moviePage,
    isLoading,
    isError,
    error,
  } = useQuery<MoviePage>({
    queryKey: ["movies"],
    queryFn: fetchMovies,
  });

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (isError || !moviePage) {
    return <div>{JSON.stringify(error)}</div>;
  }

  console.log(moviePage);

  return (
    <main className="mx-5 mt-10 text-white lg:mx-10">
      <div className="flex flex-col justify-between">
        <div className="grid grid-cols-2 gap-x-3.5 gap-y-7 sm:grid-cols-[repeat(auto-fill,minmax(190px,1fr))]">
          {moviePage.content.map((movie) => (
            <MoviePreview key={movie.movieId} moviePreview={movie} />
          ))}
        </div>
      </div>
    </main>
  );
};

export default Home;
