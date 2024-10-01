import React, { type FC, useState } from "react";
import { SendHorizontal } from "lucide-react";
import { Client } from "stompjs";

interface ChatInputProps {
  client: Client | null;
}

const ChatInput: FC<ChatInputProps> = ({ client }) => {
  const [text, setText] = useState<string>("");

  const sendMessage = (text: string) => {
    if (client) {
      client.send("/app/chat", {}, text);
      setText("");
    }
  };

  const handleEnterKey = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      sendMessage(text);
    }
  };

  return (
    <div className="absolute bottom-2 left-2 right-2 flex min-h-11 gap-2">
      <input
        type="text"
        placeholder="Write a message"
        className="w-full rounded-full border-transparent bg-gray-700 px-4 text-white placeholder:text-gray-400 focus:border-cyan-300 focus:outline-none"
        value={text}
        onChange={(e) => setText(e.target.value)}
        onKeyDown={handleEnterKey}
      />
      <button
        className="p-2 text-white hover:rounded-full hover:bg-gray-700"
        onClick={() => sendMessage(text)}
      >
        <SendHorizontal strokeWidth={1} size={26} className="fill-cyan-600" />
      </button>
    </div>
  );
};

export default ChatInput;
