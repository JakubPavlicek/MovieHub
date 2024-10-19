import { useAuth0 } from "@auth0/auth0-react";
import type { Middleware } from "openapi-fetch";

export const useAuthMiddleware = () => {
  const { isLoading, isAuthenticated, getAccessTokenSilently } = useAuth0();

  const authMiddleware: Middleware = {
    async onRequest({ request }) {
      if (!isLoading && isAuthenticated) {
        const accessToken = await getAccessTokenSilently();
        request.headers.set("Authorization", `Bearer ${accessToken}`);
      }
      return request;
    },

    async onResponse({ response }) {
      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`API Error: ${response.status} ${errorMessage}`);
      }
      return response;
    },
  };

  return authMiddleware;
};
