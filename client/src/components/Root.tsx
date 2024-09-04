import React from "react";
import Header from "./header/Header.tsx";
import { Outlet } from "react-router-dom";
import Footer from "./footer/Footer.tsx";

const Root: React.FC = () => {
  return (
    <>
      <Header />
      <Outlet />
      <Footer />
    </>
  );
};

export default Root;
