import React, { type FC, useState } from "react";
import { SendHorizontal } from "lucide-react";

interface ChatInputProps {
  sendMessage: (message: string) => void;
}

const ChatInput: FC<ChatInputProps> = ({ sendMessage }) => {
  const [message, setMessage] = useState<string>("");

  const submitMessage = (text: string) => {
    sendMessage(text);
    setMessage("");
  };

  const handleEnterKey = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      e.preventDefault();
      submitMessage(message);
    }
  };

  return (
    <div className="absolute bottom-2 left-2 right-2 flex min-h-11 gap-2">
      <input
        type="text"
        placeholder="Write a message"
        className="w-full rounded-full border-transparent bg-gray-700 px-4 text-white placeholder:text-gray-400 focus:border-cyan-300 focus:outline-none"
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        onKeyDown={handleEnterKey}
      />
      <button
        className="p-2 text-white hover:rounded-full hover:bg-gray-700"
        onClick={() => submitMessage(message)}
      >
        <SendHorizontal strokeWidth={1} size={26} className="fill-cyan-600" />
      </button>
    </div>
  );
};

export default ChatInput;
