import { useAuth0 } from "@auth0/auth0-react";
import type { Middleware } from "openapi-fetch";

export const useAuthMiddleware = () => {
  const { getAccessTokenSilently, isAuthenticated } = useAuth0();

  const authMiddleware: Middleware = {
    async onRequest({ request }) {
      if (isAuthenticated) {
        const token = await getAccessTokenSilently();
        request.headers.set("Authorization", `Bearer ${token}`);
      }
      console.log(request);
      return request;
    },
  };

  return authMiddleware;
};
