import type { FC } from "react";
import { Outlet } from "react-router-dom";
import Header from "./header/Header.tsx";
import Footer from "./footer/Footer.tsx";

const Root: FC = () => {
  return (
    <>
      <Header />
      <Outlet />
      <Footer />
    </>
  );
};

export default Root;
