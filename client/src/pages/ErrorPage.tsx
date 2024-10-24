import type { FC } from "react";
import { isRouteErrorResponse, NavLink, useRouteError } from "react-router-dom";
import { CircleChevronLeft } from "lucide-react";
import { useTranslation } from "react-i18next";

export const ErrorPage: FC = () => {
  const { t } = useTranslation();
  const error = useRouteError();

  if (isRouteErrorResponse(error)) {
    return (
      <main className="mx-4 flex h-screen flex-col items-center justify-center gap-4">
        <h1 className="text-7xl font-bold text-gray-400">{error.status}</h1>
        <p className="text-xl font-medium text-gray-500">{t("components.page.error.pageNotFound")}</p>
        <NavLink to="/" className="mt-2">
          <button className="inline-flex items-center rounded-2xl bg-cyan-600 px-3 py-2 text-white hover:bg-cyan-500 active:bg-cyan-400">
            <CircleChevronLeft className="mr-2" />
            {t("components.page.error.backToHomepage")}
          </button>
        </NavLink>
      </main>
    );
  }

  return <div>{t("components.page.error.somethingWentWrong")}</div>;
};
