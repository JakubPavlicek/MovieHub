import { CategoryItemType, CategoryItem } from "@/types/category.ts";

export function destination(type: CategoryItemType, item: CategoryItem) {
  return `/${type}/${item.name.toLowerCase()}`;
}
