import { type FC, useEffect, useRef } from "react";
import { useAuth0 } from "@auth0/auth0-react";
import type { Message } from "@/types/message";

interface ChatMessageProps {
  username: string;
  pictureUrl: string;
  text: string;
  time: string;
  isCurrentUser?: boolean;
}

const ChatMessage: FC<ChatMessageProps> = ({ username, pictureUrl, text, time, isCurrentUser }) => {
  return (
    <div className={`mr-2 flex flex-col gap-1 ${isCurrentUser ? "items-end" : ""}`}>
      {!isCurrentUser && (
        <div className="mx-12 mr-3 flex items-center gap-2 text-xs">
          <span className="truncate text-gray-300">{username}</span>
          <span className="text-gray-400">{time}</span>
        </div>
      )}
      <div className="flex items-end gap-2">
        {!isCurrentUser && <img src={pictureUrl} alt={username} className="aspect-square h-8 w-8 rounded-full" />}
        <span
          className={`min-h-fit max-w-52 break-words rounded-2xl px-3 py-2 ${isCurrentUser ? "bg-cyan-600" : "bg-gray-600"}`}
        >
          {text}
        </span>
      </div>
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
    <div className="m-2 mt-0 flex h-[21rem] flex-col gap-3 overflow-hidden overflow-y-scroll">
      <div className="mt-2" />
      {messages.map((message, i) => (
        <ChatMessage
          key={`${i}-${message.userId}-${message.time}`}
          username={message.username}
          pictureUrl={message.pictureUrl}
          time={message.time}
          text={message.message}
          isCurrentUser={user?.sub === message.userId}
        />
      ))}
      <div ref={messagesEndRef} />
    </div>
  );
};
