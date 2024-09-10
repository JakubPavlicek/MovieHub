import { useQuery } from "@tanstack/react-query";

export interface IMoviePreview {
  id: number;
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
  return (await data.json()) as MoviePage;
}

const useMovies = () => {
  const {
    data: moviePage,
    isLoading,
    isError,
    error,
  } = useQuery<MoviePage>({
    queryKey: ["movies"],
    queryFn: fetchMovies,
  });

  return { moviePage, isLoading, isError, error };
};

export default useMovies;
