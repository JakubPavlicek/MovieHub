import { CategoryItemType, CategoryItem } from "@/types/category.ts";

export function resolveCategoryPath(type: CategoryItemType, item: CategoryItem): string {
  return `/${type}/${item.name.toLowerCase()}`;
}
