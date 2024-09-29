import type { FC } from "react";
import { SendHorizontal } from "lucide-react";

const ChatInput: FC = () => {
  return (
    <div className="absolute bottom-2 left-2 right-2 flex min-h-11 gap-2">
      <input
        type="text"
        placeholder="Write a message"
        className="w-full rounded-full border-transparent bg-gray-700 px-4 text-white placeholder:text-gray-400 focus:border-teal-300 focus:outline-none"
      />
      <button className="p-2 text-white hover:rounded-full hover:bg-gray-700">
        <SendHorizontal strokeWidth={1} size={26} className="fill-cyan-600" />
      </button>
    </div>
  );
};

export default ChatInput;
