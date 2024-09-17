import { CategoryItem } from "@/types/category.ts";

type Genre = CategoryItem;

export interface GenreResponse {
  genres: Genre[];
}
