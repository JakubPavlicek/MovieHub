import { useCallback, useEffect, useState } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import { webSocketService } from "@/services/webSocketService";
import type { Message } from "@/types/message";

export const useChat = () => {
  const [messages, setMessages] = useState<Message[]>([]);
  const { isAuthenticated, getAccessTokenSilently } = useAuth0();
  const wsService = webSocketService();

  const addMessage = useCallback((message: string) => {
    setMessages((prevMessages) => [...prevMessages, JSON.parse(message)]);
  }, []);

  const sendMessage = useCallback(
    (message: string) => {
      wsService.sendMessage("/app/chat", message);
    },
    [wsService],
  );

  const initializeWebSocket = useCallback(async () => {
    if (!isAuthenticated) return;

    const token = await getAccessTokenSilently();

    wsService.connect(token, () => {
      wsService.subscribe("/topic/chat", addMessage);
    });
  }, [isAuthenticated, getAccessTokenSilently, wsService, addMessage]);

  useEffect(() => {
    initializeWebSocket();

    return () => wsService.disconnect();
  }, [initializeWebSocket, wsService]);

  return { messages, sendMessage };
};
