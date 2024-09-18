import type { FC } from "react";
import { isRouteErrorResponse, NavLink, useRouteError } from "react-router-dom";
import { CircleChevronLeft } from "lucide-react";

const ErrorPage: FC = () => {
  const error = useRouteError();

  if (isRouteErrorResponse(error)) {
    return (
      <div className="mx-4 flex h-screen flex-col items-center justify-center gap-4">
        <h1 className="text-7xl font-bold text-gray-400">{error.status}</h1>
        <p className="text-xl font-medium text-gray-500">This page could not be found.</p>
        <NavLink to="/" className="mt-2">
          <button className="inline-flex items-center rounded-2xl bg-teal-600 px-3 py-2 text-white hover:bg-teal-500 active:bg-teal-400">
            <CircleChevronLeft className="mr-2" />
            Back To Homepage
          </button>
        </NavLink>
      </div>
    );
  }

  return <div>Something went wrong!</div>;
};

export default ErrorPage;
