import { DropdownItem } from "@/types/navigation.ts";

type Country = DropdownItem;

export interface CountryReponse {
  countries: Country[];
}
