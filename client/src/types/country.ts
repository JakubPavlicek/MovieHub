import { CategoryItem } from "@/types/category.ts";

type Country = CategoryItem;

export interface CountryReponse {
  countries: Country[];
}
