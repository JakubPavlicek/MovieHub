import { useAuth0 } from "@auth0/auth0-react";
import { useStompClient } from "react-stomp-hooks";
import { useCallback, useEffect, useState } from "react";
import type { Message } from "@/types/message";
import { useTranslation } from "react-i18next";

export const useChat = () => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [accessToken, setAccessToken] = useState("");
  const [isSubscribed, setIsSubscribed] = useState(false);
  const { t } = useTranslation();
  const { isLoading, isAuthenticated, getAccessTokenSilently } = useAuth0();
  const stompClient = useStompClient();

  useEffect(() => {
    if (!isAuthenticated) return;

    const fetchToken = async () => {
      const token = await getAccessTokenSilently();
      setAccessToken(token);
    };

    fetchToken();
  }, [isAuthenticated, getAccessTokenSilently]);

  const addMessage = useCallback((message: string) => {
    setMessages((prevMessages) => [...prevMessages, JSON.parse(message)]);
  }, []);

  const subscribe = useCallback(() => {
    if (!stompClient || isSubscribed) return;

    stompClient.subscribe("/topic/chat", (message) => addMessage(message.body), {
      Authorization: `Bearer: ${accessToken}`,
    });

    setIsSubscribed(true);
  }, [accessToken, stompClient, addMessage, isSubscribed]);

  const sendMessage = useCallback(
    (text: string) => {
      if (!stompClient || !isAuthenticated) return;

      stompClient.publish({
        destination: "/app/chat",
        body: text,
        headers: {
          Authorization: `Bearer: ${accessToken}`,
        },
      });
    },
    [accessToken, stompClient, isAuthenticated],
  );

  useEffect(() => {
    if (!isAuthenticated && !isLoading) return;

    subscribe();
  }, [isAuthenticated, isLoading, subscribe, t]);

  return { messages, sendMessage };
};
