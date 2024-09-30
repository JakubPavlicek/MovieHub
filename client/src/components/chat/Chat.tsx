import { type FC, useEffect, useState } from "react";
import ChatButton from "@/components/chat/ChatButton";
import ChatHeader from "@/components/chat/ChatHeader";
import ChatMessages from "@/components/chat/ChatMessages";
import ChatInput from "@/components/chat/ChatInput";
import Stomp, { Client } from "stompjs";
import { useAuth0 } from "@auth0/auth0-react";
import SockJS from "sockjs-client";

export interface Message {
  message: string;
  time: string;
}

const Chat: FC = () => {
  const [isChatOpened, setIsChatOpened] = useState<boolean>(false);

  const [messages, setMessages] = useState<Message[]>([]);
  const [client, setClient] = useState<Client | null>(null);
  const { isAuthenticated, getAccessTokenSilently } = useAuth0();

  useEffect(() => {
    const connectWebSocket = async () => {
      if (!isAuthenticated) {
        return;
      }

      const socket = new SockJS("http://localhost:8088/ws");
      const client = Stomp.over(socket);

      const token = await getAccessTokenSilently();
      const authHeader = { Authorization: "Bearer " + token };

      client.debug = (msg) => console.log(msg);

      client.connect(authHeader, (frame) => {
        console.log("Connected: ", frame);

        client.subscribe(
          "/topic/chat",
          (message) => {
            const receivedMessage = JSON.parse(message.body);
            setMessages((prevMessages) => [...prevMessages, receivedMessage]);
          },
          authHeader,
        );
      });

      setClient(client);
    };

    connectWebSocket();

    client?.send("/app/chat", {}, "ahoj");

    // return () => {
    //   if (client?.connected) {
    //     client.disconnect(() => {
    //       console.log("disconnected");
    //     });
    //   }
    // };
  }, [isAuthenticated]);

  return (
    <>
      <ChatButton isChatOpened={isChatOpened} setIsChatOpened={setIsChatOpened} />
      {isChatOpened && (
        <div className="absolute bottom-24 right-6 h-[30rem] w-80 rounded-2xl bg-gray-800">
          <ChatHeader />
          <ChatMessages messages={messages} />
          <ChatInput client={client} />
        </div>
      )}
    </>
  );
};

export default Chat;
