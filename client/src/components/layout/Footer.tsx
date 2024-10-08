import { type FC, memo } from "react";
import { Logo } from "@/components/header/Logo";

export const Footer: FC = memo(() => {
  return (
    <footer className="mx-auto mb-6 mt-10 text-neutral-400 2xl:container">
      <div className="mx-5">
        <hr className="w-full border-neutral-700" />
      </div>
      <div className="mx-5 mt-8 flex flex-col gap-6">
        <Logo />
        <div>
          <p className="font-semibold text-neutral-300">&copy; 2024 MovieHub. All Rights Reserved.</p>
          <p className="mt-2">
            Discover your next favorite movie with us! Stay updated with the latest releases, news, and reviews. Join
            our community of movie enthusiasts and share your passion for cinema.
          </p>
          <p className="mt-4">For inquiries or feedback, please contact our support team.</p>
          <p className="mt-4 italic">MovieHub is the perfect place for all your movie needs. Enjoy watching!</p>
        </div>
      </div>
    </footer>
  );
});
