import "@/styles/index.css";
import "react-toastify/dist/ReactToastify.min.css";
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
import { ApiProvider } from "@/context/ApiProvider";
import { ToastContainer } from "react-toastify";
import { DirectorPage } from "@/pages/DirectorPage";
import { ProductionPage } from "@/pages/ProductionPage";
import { CastPage } from "@/pages/CastPage";
import { FilterPage } from "@/pages/FilterPage";

// https://auth0.com/blog/complete-guide-to-react-user-authentication/

const queryClient = new QueryClient();

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "movies",
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
        path: "director/:directorName",
        element: <DirectorPage />,
      },
      {
        path: "production/:companyName",
        element: <ProductionPage />,
      },
      {
        path: "cast/:actorName",
        element: <CastPage />,
      },
      {
        path: "filter",
        element: <FilterPage />,
      },
      {
        path: "search/:keyword",
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
      cacheLocation="localstorage"
    >
      <ApiProvider>
        <QueryClientProvider client={queryClient}>
          <RouterProvider router={router} />
          {/*<ReactQueryDevtools initialIsOpen={false} />*/}
          <ToastContainer theme="colored" autoClose={2500} />
        </QueryClientProvider>
      </ApiProvider>
    </Auth0Provider>
  </StrictMode>,
);
