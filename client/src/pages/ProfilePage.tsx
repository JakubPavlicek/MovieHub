import type { FC } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import { MovieSkeleton } from "@/components/common/MovieSkeleton";
import { useTranslation } from "react-i18next";

const UserInfo: FC<{ label: string; value: string }> = ({ label, value }) => (
  <div className="grid grid-cols-[150px_1fr] sm:grid-cols-[200px_1fr]">
    <span className="font-medium">{label}</span>
    <span className="truncate text-left font-serif">{value}</span>
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
      <div className="mx-5 mt-10 flex justify-center">
        <div className="w-full max-w-2xl rounded-3xl border p-6 sm:p-10 md:p-12 lg:p-16">
          <div className="flex flex-col text-white">
            <div className="flex flex-col items-center gap-4">
              <img src={picture} alt={name} className="aspect-square h-32 w-32 rounded-full sm:h-40 sm:w-40" />
              <span className="font-serif text-xl font-bold sm:text-2xl">{nickname}</span>
              <span className="text-base text-neutral-300 sm:text-lg">{email}</span>
            </div>
            <div className="flex justify-center">
              <div className="mt-8 flex flex-col gap-6 text-lg sm:text-xl">
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
