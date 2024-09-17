import "./index.css";
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Auth0Provider } from "@auth0/auth0-react";
import Root from "@/components/Root.tsx";
import Error from "@/pages/Error.tsx";
import HomePage from "@/pages/HomePage.tsx";
import GenrePage from "@/pages/GenrePage.tsx";
import CountryPage from "@/pages/CountryPage.tsx";
import SearchPage from "@/pages/SearchPage.tsx";

// https://auth0.com/blog/complete-guide-to-react-user-authentication/

const queryClient = new QueryClient();

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    errorElement: <Error />,
    children: [
      {
        path: "",
        element: <HomePage />,
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
      }}
    >
      <QueryClientProvider client={queryClient}>
        <RouterProvider router={router} />
      </QueryClientProvider>
    </Auth0Provider>
  </StrictMode>,
);
