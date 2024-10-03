import "@/styles/index.css";
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Auth0Provider } from "@auth0/auth0-react";
import { Root } from "@/components/layout/Root";
import { HomePage } from "@/pages/HomePage";
import { GenrePage } from "@/pages/GenrePage";
import { CountryPage } from "@/pages/CountryPage";
import { SearchPage } from "@/pages/SearchPage";
import { ErrorPage } from "@/pages/ErrorPage";
import { MoviePage } from "@/pages/MoviePage";
import type { paths } from "@/api/api";
import createFetchClient from "openapi-fetch";
import createClient from "openapi-react-query";

// https://auth0.com/blog/complete-guide-to-react-user-authentication/

const queryClient = new QueryClient();

const fetchClient = createFetchClient<paths>({
  baseUrl: "http://localhost:8088/",
});

export const api = createClient(fetchClient);

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "",
        element: <HomePage />,
      },
      {
        path: "movie/:movieId",
        element: <MoviePage />,
      },
      {
        path: "genre/:genreName",
        element: <GenrePage />,
      },
      {
        path: "country/:countryName",
        element: <CountryPage />,
      },
      {
        path: "search/:query",
        element: <SearchPage />,
      },
    ],
  },
]);

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <Auth0Provider
      domain={import.meta.env.VITE_AUTH0_DOMAIN}
      clientId={import.meta.env.VITE_AUTH0_CLIENT_ID}
      authorizationParams={{
        redirect_uri: window.location.origin,
        audience: import.meta.env.VITE_AUTH0_AUDIENCE,
      }}
    >
      <QueryClientProvider client={queryClient}>
        <RouterProvider router={router} />
      </QueryClientProvider>
    </Auth0Provider>
  </StrictMode>,
);
