import { DropdownItem } from "@/types/navigation.ts";

type Genre = DropdownItem;

export interface GenreResponse {
  genres: Genre[];
}
