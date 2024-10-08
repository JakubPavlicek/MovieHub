import type { FC } from "react";
import { Logo } from "@/components/header/Logo";

export const Footer: FC = () => {
  return (
    <footer className="mx-5 mb-6 mt-10 text-neutral-400">
      <hr className="w-full border-neutral-600" />
      <div className="mt-5 flex flex-col gap-6">
        <Logo />
        <div>
          <p className="font-semibold text-neutral-300">&copy; 2024 MovieHub. All Rights Reserved.</p>
          <p className="mt-2">
            Discover your next favorite movie with us! Stay updated with the latest releases, news, and reviews. Join
            our community of movie enthusiasts and share your passion for cinema.
          </p>
          <p className="mt-4">For inquiries or feedback, please contact our support team.</p>
          <p className="mt-4 italic">MovieHub is your ultimate destination for all things movies. Enjoy watching!</p>
        </div>
      </div>
    </footer>
  );
};
