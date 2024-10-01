import { type FC, useEffect, useRef } from "react";
import type { Message } from "@/types/message";
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

interface ChatMessagesProps {
  messages: Message[];
}

export const ChatMessages: FC<ChatMessagesProps> = ({ messages }) => {
  const { user } = useAuth0();
  const messagesEndRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (messagesEndRef.current) {
      messagesEndRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages]);

  return (
    <div className="m-2 mt-0 flex h-[21rem] flex-col gap-4 overflow-hidden overflow-y-scroll">
      {messages.map((message, i) => (
        <ChatMessage
          key={i}
          name={message.username}
          time={message.time}
          text={message.message}
          isCurrentUser={user?.nickname == message.username}
        />
      ))}
      <div ref={messagesEndRef} />
    </div>
  );
};
