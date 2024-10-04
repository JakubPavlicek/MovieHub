import { createContext, ReactNode, useContext, useMemo } from "react";
import createFetchClient from "openapi-fetch";
import createClient, { type OpenapiQueryClient } from "openapi-react-query";
import type { paths } from "@/api/api";
import { useAuthMiddleware } from "@/hooks/useAuthMiddleware";

// Define the context type
interface ApiContextProps {
  api: OpenapiQueryClient<paths, `${string}/${string}`>;
}

// Create the API context
const ApiContext = createContext<ApiContextProps | undefined>(undefined);

// Provider component for the API context
export const ApiProvider = ({ children }: { children: ReactNode }) => {
  const authMiddleware = useAuthMiddleware();

  const fetchClient = createFetchClient<paths>({
    baseUrl: "http://localhost:8088/",
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
