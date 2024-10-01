import Stomp from "stompjs";
import SockJS from "sockjs-client";

interface WebSocketService {
  connect: (token: string, connectCallback: () => void) => void;
  subscribe: (destination: string, onMessageReceived: (message: string) => void) => void;
  disconnect: () => void;
  sendMessage: (destination: string, message: string) => void;
}

export const webSocketService = (): WebSocketService => {
  let client: Stomp.Client | null = null;

  const connect = (token: string, connectCallback: () => void) => {
    const socket = new SockJS("http://localhost:8088/ws");
    client = Stomp.over(socket);

    const authHeader = { Authorization: `Bearer ${token}` };

    client.connect(authHeader, connectCallback);
  };

  const subscribe = (destination: string, onMessageReceived: (message: string) => void) => {
    if (client?.connected) {
      client.subscribe(destination, (message) => {
        onMessageReceived(message.body);
      });
    }
  };

  const disconnect = () => {
    if (client?.connected) {
      client.disconnect(() => {});
    }
  };

  const sendMessage = (destination: string, message: string) => {
    if (client?.connected) {
      client.send(destination, {}, message);
    }
  };

  return { connect, subscribe, disconnect, sendMessage };
};
