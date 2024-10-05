import { createContext, ReactNode, useContext, useMemo } from "react";
import createFetchClient from "openapi-fetch";
import createClient, { type OpenapiQueryClient } from "openapi-react-query";
import { useAuthMiddleware } from "@/hooks/useAuthMiddleware";
import type { paths } from "@/api/api";

interface ApiContextProps {
  api: OpenapiQueryClient<paths, `${string}/${string}`>;
}

const ApiContext = createContext<ApiContextProps | undefined>(undefined);

export const ApiProvider = ({ children }: { children: ReactNode }) => {
  const authMiddleware = useAuthMiddleware();

  const fetchClient = createFetchClient<paths>({
    baseUrl: "http://localhost:8080/",
  });

  fetchClient.use(authMiddleware);

  const api = useMemo(() => createClient(fetchClient), [fetchClient]);
  const apiValue = useMemo(() => ({ api }), [api]);

  return <ApiContext.Provider value={apiValue}>{children}</ApiContext.Provider>;
};

export const useApi = () => {
  const context = useContext(ApiContext);

  if (!context) {
    throw new Error("useApi must be used within an ApiProvider");
  }

  return context.api;
};
