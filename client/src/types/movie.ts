import type { Director } from "@/types/director";
import type { Actor } from "@/types/actor";
import type { ProductionCompany } from "@/types/productionCompany";
import type { Genre } from "@/types/genre";
import type { Country } from "@/types/country";

export interface MovieDetails {
  id: string;
  name: string;
  releaseDate: string;
  duration: number;
  description: string;
  rating: number;
  reviewCount: number;
  posterUrl: string;
  trailerUrl: string;
  director: Director;
  cast: Actor[];
  productionCompanies: ProductionCompany[];
  genres: Genre[];
  countries: Country[];
}

export interface MovieSearchParams {
  page?: number;
  limit?: number;
  sort?: string;
  name?: string;
  releaseDate?: string;
  duration?: number;
  description?: string;
  director?: string;
  actors?: string;
  genres?: string;
  countries?: string;
  keyword?: string;
}

export interface MoviePreview {
  id: number;
  name: string;
  releaseYear: number;
  duration: string;
  posterUrl: string;
  genres: string[];
}

export interface MovieRating {
  rating: number;
}

export interface MoviePage {
  content: MoviePreview[];
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

export interface Pageable {
  pageNumber: number;
  pageSize: number;
  sort: Sort;
  offset: number;
  paged: boolean;
  unpaged: boolean;
}

export interface Sort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}
