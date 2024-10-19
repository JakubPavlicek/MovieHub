import { useAuth0 } from "@auth0/auth0-react";
import { useStompClient } from "react-stomp-hooks";
import { useCallback, useEffect, useState } from "react";
import type { Message } from "@/types/message";

export const useChat = () => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [accessToken, setAccessToken] = useState("");
  const [isSubscribed, setIsSubscribed] = useState(false);
  const { isAuthenticated, getAccessTokenSilently } = useAuth0();
  const stompClient = useStompClient();

  useEffect(() => {
    const fetchToken = async () => {
      const token = await getAccessTokenSilently();
      setAccessToken(token);
    };

    fetchToken();
  }, [getAccessTokenSilently]);

  const addMessage = useCallback((message: string) => {
    setMessages((prevMessages) => [...prevMessages, JSON.parse(message)]);
  }, []);

  const subscribe = useCallback(() => {
    if (!stompClient || !isAuthenticated || isSubscribed) return;

    stompClient.subscribe("/topic/chat", (message) => addMessage(message.body), {
      Authorization: `Bearer: ${accessToken}`,
    });

    setIsSubscribed(true);
  }, [accessToken, stompClient, isAuthenticated, addMessage, isSubscribed]);

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
    subscribe();
  }, [subscribe]);

  return { messages, sendMessage };
};
