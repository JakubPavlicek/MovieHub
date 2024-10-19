import { useAuth0 } from "@auth0/auth0-react";
import { ReactNode, useEffect, useState } from "react";
import { StompSessionProvider } from "react-stomp-hooks";

export const AuthStompProvider = ({ children }: { children: ReactNode }) => {
  const { getAccessTokenSilently, isAuthenticated } = useAuth0();
  const [accessToken, setAccessToken] = useState("");

  useEffect(() => {
    if (!isAuthenticated) return;

    const fetchToken = async () => {
      const token = await getAccessTokenSilently();
      setAccessToken(token);
    };

    fetchToken();
  }, [isAuthenticated, getAccessTokenSilently]);

  return (
    <StompSessionProvider
      url={`${import.meta.env.VITE_API_BASE_URL}/ws`}
      connectHeaders={{
        Authorization: `Bearer ${accessToken}`,
      }}
      enabled={isAuthenticated}
    >
      {children}
    </StompSessionProvider>
  );
};
