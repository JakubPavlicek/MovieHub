import type { FC } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import { MovieSkeleton } from "@/components/common/MovieSkeleton";
import { useTranslation } from "react-i18next";

const UserInfo: FC<{ label: string; value: string }> = ({ label, value }) => (
  <div className="grid grid-cols-[150px_1fr] sm:grid-cols-[200px_1fr]">
    <span className="font-medium">{label}</span>
    <span className="text-left font-serif">{value}</span>
  </div>
);

export const ProfilePage: FC = () => {
  const { t } = useTranslation();
  const { user } = useAuth0();

  if (!user) {
    return <MovieSkeleton />;
  }

  const { picture, nickname, name, address, birthdate, email } = user;
  const notProvided = t("components.page.profile.notProvided");

  const userInfoList = [
    { label: t("components.page.profile.name"), value: name ?? notProvided },
    { label: t("components.page.profile.email"), value: email ?? notProvided },
    { label: t("components.page.profile.address"), value: address ?? notProvided },
    { label: t("components.page.profile.nickname"), value: nickname ?? notProvided },
    { label: t("components.page.profile.birthDate"), value: birthdate ?? notProvided },
  ];

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mt-10 flex justify-center">
        <div className="rounded-3xl border p-8 sm:p-20">
          <div className="flex flex-col text-white">
            <div className="flex flex-col items-center gap-3">
              <img src={picture} alt={name} className="aspect-square h-40 w-40 rounded-full" />
              <span className="font-serif text-2xl font-bold">{nickname}</span>
              <span className="text-lg text-neutral-300">{email}</span>
            </div>
            <div className="flex justify-center">
              <div className="mt-10 flex flex-col gap-8 text-xl">
                {userInfoList.map(({ label, value }) => (
                  <UserInfo key={label} label={label} value={value} />
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
};