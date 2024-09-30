import { type FC, useEffect, useState } from "react";
import SockJS from "sockjs-client";
import Stomp, { Client } from "stompjs";
import { useAuth0 } from "@auth0/auth0-react";

interface ChatMessageProps {
  name: string;
  time: string;
  text: string;
  isCurrentUser?: boolean;
}

const ChatMessage: FC<ChatMessageProps> = ({ name, time, text, isCurrentUser }) => {
  return (
    <div className={`mr-2 flex flex-col gap-1 ${isCurrentUser ? "items-end" : ""}`}>
      <div className="mx-3 flex w-[11.5rem] items-center gap-2 text-xs">
        <span className="truncate text-gray-300">{name}</span>
        <span className="text-gray-400">{time}</span>
      </div>
      <span
        className={`min-h-20 w-52 rounded-2xl px-3 py-2 ${isCurrentUser ? "bg-cyan-600" : "bg-gray-600"}`}
      >
        {text}
      </span>
    </div>
  );
};

const ChatMessages: FC = () => {
  const [messages, setMessages] = useState<string[]>([]);
  const [client, setClient] = useState<Client | null>(null);
  const { getAccessTokenSilently } = useAuth0();

  useEffect(() => {
    const connectWebSocket = async () => {
      const socket = new SockJS("http://localhost:8088/ws");
      const client = Stomp.over(socket);

      const token = await getAccessTokenSilently();
      const authHeader = { Authorization: "Bearer " + token };

      client.debug = (msg) => console.log(msg);

      client.connect(authHeader, (frame) => {
        console.log("Connected: ", frame);

        // client.subscribe(
        //   "/topic/chat",
        //   (message) => {
        //     const receivedMessage = JSON.parse(message.body);
        //     setMessages((prevMessages) => [...prevMessages, receivedMessage]);
        //   },
        //   authHeader,
        // );
      });

      setClient(client);
    };

    connectWebSocket();

    // return () => {
    //   if (client?.connected) {
    //     client.disconnect(() => {
    //       console.log("disconnected");
    //     });
    //   }
    // };
  }, []);

  return (
    <div className="m-2 mt-0 flex h-[21rem] flex-col gap-4 overflow-hidden overflow-y-scroll">
      <ChatMessage name={"user"} time={"22:49"} text={"text"} isCurrentUser={false} />
      <ChatMessage name={"user"} time={"22:49"} text={"text"} isCurrentUser={true} />
    </div>
  );
};

export default ChatMessages;
