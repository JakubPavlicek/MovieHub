import { type FC, memo } from "react";
import { Logo } from "@/components/header/desktop/Logo";
import { useTranslation } from "react-i18next";

export const Footer: FC = memo(() => {
  const { t } = useTranslation();
  return (
    <footer className="mx-auto mb-6 mt-10 text-neutral-400 2xl:container">
      <div className="mx-5">
        <hr className="w-full border-neutral-700" />
      </div>
      <div className="mx-5 mt-8 flex flex-col gap-6">
        <div className="max-w-fit">
          <Logo />
        </div>
        <div>
          <p className="font-semibold text-neutral-300">{t("components.footer.copyright")}</p>
          <p className="mt-2">{t("components.footer.description")}</p>
          <p className="mt-4">{t("components.footer.inquiries")}</p>
          <p className="mt-4 italic">{t("components.footer.tagline")}</p>
        </div>
      </div>
    </footer>
  );
});
