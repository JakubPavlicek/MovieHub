import { type FC } from "react";
import { Outlet } from "react-router-dom";
import { Header } from "@/components/layout/Header";
import { Footer } from "@/components/layout/Footer";
import { Chat } from "@/components/chat/Chat";

export const Root: FC = () => {
  return (
    <>
      <Header />
      <Outlet />
      <Chat />
      <Footer />
    </>
  );
};
