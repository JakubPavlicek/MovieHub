import { useApi } from "@/context/ApiProvider";

export const useYears = () => {
  const api = useApi();
  const { data } = api.useQuery("get", "/movies/years");

  const years = data?.years ?? [];

  return { years };
};
